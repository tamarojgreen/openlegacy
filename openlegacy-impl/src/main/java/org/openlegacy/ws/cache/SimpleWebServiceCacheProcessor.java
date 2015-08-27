/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.ws.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.WebServicesRegistry;
import org.openlegacy.utils.XmlSerializationUtil;
import org.openlegacy.utils.ZipUtil;
import org.openlegacy.ws.definitions.WebServiceDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

public class SimpleWebServiceCacheProcessor implements WebServiceCacheProcessor, MethodInterceptor {

	private static final Log logger = LogFactory.getLog(SimpleWebServiceCacheProcessor.class);

	public static final int OPERATION_ADD = 1;
	public static final int OPERATION_REMOVE = 2;

	public static class QueueOperation {

		int code;
		Object[] args;

		public QueueOperation(int code, Object... args) {
			this.code = code;
			this.args = args;
		}
	}

	public static interface CallBack {

		public void callBack();
	}

	@Inject
	WebServicesRegistry wsRegistry;

	@Inject
	ApplicationContext applicationContext;

	private MessageDigest messageDigest;

	private int semaphoreWaitTimeOut = 10;

	private volatile BlockingQueue<QueueOperation> operationQueue = new LinkedBlockingDeque<QueueOperation>();
	private volatile Semaphore lockQueue = new Semaphore(1, true), unlockQueue = new Semaphore(1, true);
	private volatile Map<String, Semaphore> requestSemaphores = new LinkedHashMap<String, Semaphore>();
	private volatile boolean destroying = false;

	private boolean disableCaching = false;
	private boolean preProcessCacheObject;
	private WebServiceCacheEngine cacheEngine;

	private volatile int lastError = WebServiceCacheErrorConverter.CACHE_PROCESSOR_INIT_ERROR;

	private CallBack operationQueueRunnableCallBack = new CallBack() {

		@Override
		public void callBack() {
			continueDestroy();
		}
	};

	private Runnable operationQueueRunnable = new Runnable() {

		@Override
		public void run() {
			while (!destroying) {
				QueueOperation q = null;
				try {
					getEngineLastError();
					if (hasErrors() || operationQueue.isEmpty()) {
						if (!operationQueue.isEmpty()) {
							operationQueue.clear();
						}
						Thread.sleep(1);
						continue;
					}
					q = operationQueue.take();
				} catch (Exception e) {
				}

				Object[] args = q.args;
				switch (q.code) {
					case OPERATION_ADD:
						put((String)args[0], args[1], (WebServiceMethodDefinition)args[2]);
						break;
					case OPERATION_REMOVE:
						remove((String)args[0]);
						break;
				}
			}
			operationQueueRunnableCallBack.callBack();
		}
	};

	// if you don`t need compressing and serializing - change bean constructor argument to false
	public SimpleWebServiceCacheProcessor(boolean preProcessCacheObject) {
		this.preProcessCacheObject = preProcessCacheObject;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			lastError = WebServiceCacheErrorConverter.NULL_ENGINE;
		} catch (Exception e) {
			logger.error("Cannot get instance for MD5 digest. WS caching disabled.");
		}

		if (!disableCaching) {
			new Thread(operationQueueRunnable).start();
		}
	}

	@Override
	public void put(String key, Object obj, WebServiceMethodDefinition methodDefinition) {
		getEngineLastError();
		if (hasErrors()) {
			return;
		}
		WebServiceCacheData data = new WebServiceCacheData();
		// hex avoids xml in xml storage
		Object cached = preProcessCacheObject ? DatatypeConverter.printHexBinary(XmlSerializationUtil.xStreamSerialize(obj).getBytes())
				: obj;
		data.setData(cached);
		data.setExpirationTime(System.currentTimeMillis() + methodDefinition.getCacheDuration());
		cached = preProcessCacheObject ? ZipUtil.compress(XmlSerializationUtil.xStreamSerialize(data).getBytes()) : data;
		cacheEngine.put(key, cached);
		try {
			unlock(key);
		} catch (Exception e) {
			logger.error(String.format("Unable unlock semaphore for %s key", key));
			e.printStackTrace();
		}

	}

	@Override
	public Object get(String key, long accessTime) {
		getEngineLastError();
		if (hasErrors()) {
			return null;
		}
		Object cached = cacheEngine.get(key, accessTime);
		if (cached == null) {
			return null;
		}
		try {
			cached = preProcessCacheObject ? XmlSerializationUtil.xStreamDeserialize(new String(
					ZipUtil.decompress((byte[])cached))) : cached;
			WebServiceCacheData cacheData = (WebServiceCacheData)cached;
			if (accessTime > cacheData.getExpirationTime()) {
				addQueueOperation(OPERATION_REMOVE, key);
				return null;
			}

			cached = preProcessCacheObject ? XmlSerializationUtil.xStreamDeserialize(new String(
					DatatypeConverter.parseHexBinary((String)cacheData.getData()))) : cacheData.getData();
			unlock(key);
			return cached;
		} catch (Exception e) {
			addQueueOperation(OPERATION_REMOVE, key);
			return null;
		}
	}

	@Override
	public void remove(String key) {
		getEngineLastError();
		if (hasErrors()) {
			return;
		}
		cacheEngine.remove(key);
	}

	@Override
	public synchronized String generateKey(Object... args) {
		String md5 = "NoHash";
		if (args != null && args.length > 0) {
			md5 = DatatypeConverter.printHexBinary(messageDigest.digest(ZipUtil.compress(XmlSerializationUtil.xStreamSerialize(
					args).getBytes())));
		}
		return md5;
	}

	@Override
	public void setEngine(WebServiceCacheEngine cacheEngine) {
		this.cacheEngine = cacheEngine;
		lastError = WebServiceCacheErrorConverter.ENGINE_ERROR;
		if (this.cacheEngine.init()) {
			lastError = WebServiceCacheErrorConverter.ALL_OK;
		}
	}

	private void lock(String key) throws InterruptedException {
		lockQueue.acquire(1);
		Semaphore semaphore = requestSemaphores.get(key);
		if (semaphore == null) {
			semaphore = new Semaphore(1);
			requestSemaphores.put(key, semaphore);
			lockQueue.release(1);
			semaphore.acquire(1);
		} else {
			lockQueue.release(1);
			semaphore.tryAcquire(1, semaphoreWaitTimeOut, TimeUnit.SECONDS);
		}
	}

	private void unlock(String key) throws InterruptedException {
		unlockQueue.acquire(1);
		Semaphore semaphore = requestSemaphores.get(key);
		if (semaphore != null) {
			if (!semaphore.hasQueuedThreads()) {
				requestSemaphores.remove(key);
			}
			semaphore.release(1);
		}
		unlockQueue.release(1);
	}

	private void addQueueOperation(int code, Object... args) {
		operationQueue.add(new QueueOperation(code, args));
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method proxiedMethod = invocation.getMethod();
		if (invocation.getMethod().getReturnType() == void.class || hasErrors()) {
			return invocation.proceed();
		}

		WebServiceDefinition wsDef = wsRegistry.getWebServiceByClass(invocation.getMethod().getDeclaringClass());

		if (wsDef == null) {
			return invocation.proceed();
		}

		WebServiceMethodDefinition wsMDef = wsDef.getMethodByName(proxiedMethod.getName());

		if (wsMDef == null || wsMDef.getCacheDuration() == 0) {
			return invocation.proceed();
		}
		String key = String.format("%s.%s.%s", wsDef.getName(), wsMDef.getName(), generateKey(invocation.getArguments()));

		long accessTime = System.currentTimeMillis(); // pre semaphored time

		try {
			lock(key);
		} catch (Exception e) {
			logger.error(String.format(
					"Unable to process semaphore for %s key. Original method call will proceed without caching", key));
			e.printStackTrace();
			return invocation.proceed();
		}

		Object cached = get(key, accessTime);
		if (cached != null) {
			return cached;
		} else {
			Object result = null;
			logger.debug(String.format("Original call for non cached hash %s", key));
			try {
				result = invocation.proceed();
				addQueueOperation(OPERATION_ADD, key, result, wsMDef);
			} catch (Exception e) {
				unlock(key);
			}
			return result;
		}
	}

	@Override
	public void destroy() {
		destroying = true;
	}

	private void continueDestroy() {
		cacheEngine.destroy();
	}

	public void setSemaphoreWaitTimeOut(int semaphoreWaitTimeOut) {
		this.semaphoreWaitTimeOut = semaphoreWaitTimeOut;
	}

	@Override
	public synchronized String getLastError() {
		return WebServiceCacheErrorConverter.convertError(lastError);
	}

	@Override
	public void tryToFixEngine() {
		if (lastError == WebServiceCacheErrorConverter.ENGINE_ERROR) {
			cacheEngine.fix();
		}
	}

	private synchronized boolean hasErrors() {
		return lastError != WebServiceCacheErrorConverter.ALL_OK;
	}

	private synchronized void getEngineLastError() {
		if (lastError == WebServiceCacheErrorConverter.ALL_OK) {// avoid rewriting processor errors
			lastError = cacheEngine.getLastError();
		}

	}

}
