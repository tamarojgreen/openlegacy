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
import org.openlegacy.WebServicesRegistry;
import org.openlegacy.utils.ThreadWorkSeparatorUtil;
import org.openlegacy.utils.ThreadWorkSeparatorUtil.OnRun;
import org.openlegacy.utils.XmlSerializationUtil;
import org.openlegacy.utils.ZipUtil;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.WebServiceDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

public class SimpleWebServiceCacheProcessor implements WebServiceCacheProcessor, MethodInterceptor {

	private static final int ADD = 0;
	private static final int REMOVE = 1;
	private static final int UPDATE = 2;

	public static SimpleWebServiceCacheProcessor INSTANCE;

	@Inject
	private WebServicesRegistry wsRegistry;

	private static class BackGroundOperation {

		private int code;
		private Object[] data;

		public BackGroundOperation(int code, Object[] data) {
			this.code = code;
			this.data = data;
		}
	}

	private static class UpdateDuration {

		String key;
		long oldDuration, newDuration;

		public UpdateDuration(String key, long oldDuration, long newDuration) {
			this.key = key;
			this.oldDuration = oldDuration;
			this.newDuration = newDuration;
		}
	}

	public static class OnUpdateRun implements OnRun {

		SimpleWebServiceCacheProcessor wsCacheProcessor = SimpleWebServiceCacheProcessor.INSTANCE;

		@Override
		public void onRun(Object obj) {
			UpdateDuration ud = (UpdateDuration)obj;
			WebServiceCacheEngine wsCacheEngine = wsCacheProcessor.getCacheEngine();
			Object cached = wsCacheEngine.get(ud.key, -1);
			if (cached == null) {
				return;
			}
			WebServiceCacheData cacheData = (WebServiceCacheData)wsCacheProcessor.afterCache(cached, true);
			cacheData.setExpirationTime(cacheData.getExpirationTime() - ud.oldDuration + ud.newDuration);
			cached = wsCacheProcessor.beforeCache(cacheData, true);
			wsCacheEngine.update(ud.key, cached);
		}
	}

	private volatile BlockingQueue<BackGroundOperation> backGroundWork = new LinkedBlockingDeque<BackGroundOperation>();
	private volatile Semaphore lock = new Semaphore(1, true), unlock = new Semaphore(1, true);
	private volatile Map<String, Semaphore> requests = new LinkedHashMap<String, Semaphore>();
	private volatile boolean blockedLocally = false;
	private volatile int lastError = WebServiceCacheError.NULL;

	private boolean preProcessCacheObject;
	private WebServiceCacheEngine cacheEngine;
	private int semaphoreWaitTimeOut = 10;
	private MessageDigest messageDigest;

	Runnable backGroundWorker = new Runnable() {

		@Override
		public void run() {
			while (!onDestroy) {
				try {
					work();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void work() throws Exception {
			if (hasErrors()) {
				if (backGroundWork.isEmpty()) {
					backGroundWork.clear();
				}
				return;
			}
			if (backGroundWork.isEmpty()) {
				return;
			}
			BackGroundOperation work = backGroundWork.take();
			Object[] data = work.data;

			switch (work.code) {
				case ADD:
					put((String)data[0], data[1], (WebServiceMethodDefinition)data[2]);
					break;
				case REMOVE:
					remove((String)data[0]);
					break;
				case UPDATE:
					updateDuration((String)data[0], (WebServiceMethodDefinition)data[1], (Long)data[2]);
					break;
				default:
					break;
			}
		}
	};
	private volatile boolean onDestroy = false;

	public SimpleWebServiceCacheProcessor(boolean preProcessCacheObject) {
		this.preProcessCacheObject = preProcessCacheObject;
		SimpleWebServiceCacheProcessor.INSTANCE = this;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			lastError = WebServiceCacheError.CACHE_INIT_ERROR;
		}
		new Thread(backGroundWorker).start();
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method origin = invocation.getMethod();

		if (blockedLocally || origin.getReturnType() == void.class) {
			return invocation.proceed();
		}

		WebServiceDefinition wsDef = wsRegistry.getWebServiceByClass(origin.getDeclaringClass());

		if (wsDef == null) {
			return invocation.proceed();
		}

		WebServiceMethodDefinition wsMDef = wsDef.getMethodByName(origin.getName());

		if (wsMDef == null || wsMDef.getCacheDuration() == 0) {
			return invocation.proceed();
		}

		String key = String.format("%s.%s.%s", wsDef.getName(), wsMDef.getName(), generateKey(invocation.getArguments()));

		long accessTime = System.currentTimeMillis();

		lock(key);

		Object result = get(key, accessTime);
		if (result != null) {
			return result;
		} else {
			try {
				result = invocation.proceed();
				addBackGroundOperation(ADD, key, result, wsMDef);
			} catch (Exception e) {
				e.printStackTrace();
				unlock(key);
			}
			return result;
		}

	}

	@Override
	public void put(String key, Object obj, WebServiceMethodDefinition methodDefinition) {
		if (hasErrors()) {
			return;
		}
		try {
			WebServiceCacheData cacheData = new WebServiceCacheData();
			cacheData.setData(beforeCache(obj, false));
			cacheData.setExpirationTime(System.currentTimeMillis() + methodDefinition.getCacheDuration());
			cacheEngine.put(key, beforeCache(cacheData, true));
			unlock(key);
		} catch (Exception e) {
			lastError = WebServiceCacheError.CACHE_ERROR;
			e.printStackTrace();
		}
	}

	@Override
	public Object get(String key, long accessTime) {
		if (hasErrors()) {
			return null;
		}
		Object cached = cacheEngine.get(key, accessTime);
		if (cached == null) {
			return null;
		}
		try {
			WebServiceCacheData cacheData = (WebServiceCacheData)afterCache(cached, true);
			if (accessTime > cacheData.getExpirationTime()) {
				addBackGroundOperation(REMOVE, key);
				return null;
			}
			cached = afterCache(cacheData.getData(), false);
			unlock(key);
			return cached;
		} catch (Exception e) {
			e.printStackTrace();
			unlock(key);
			lastError = WebServiceCacheError.CACHE_ERROR;
			return null;
		}
	}

	@Override
	public void remove(String key) {
		if (hasErrors()) {
			return;
		}
		cacheEngine.remove(key);
	}

	@Override
	public void update(String key, Object obj) {
		cacheEngine.update(key, obj);
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
		try {
			this.cacheEngine = cacheEngine;
			if (!cacheEngine.init()) {
				lastError = WebServiceCacheError.ENGINE_INIT_ERROR;
			} else {
				lastError = WebServiceCacheError.ALL_OK;
			}
		} catch (Exception e) {
			lastError = WebServiceCacheError.ENGINE_INIT_ERROR;
		}
	}

	@Override
	public void destroy() {
		onDestroy = true;
	}

	@Override
	public int getLastError() {
		return lastError;
	}

	@Override
	public void tryToFixEngine() {
		cacheEngine.fix();
	}

	public WebServiceCacheEngine getCacheEngine() {
		return cacheEngine;
	}

	private void addBackGroundOperation(int code, Object... args) {
		backGroundWork.add(new BackGroundOperation(code, args));
	}

	public synchronized Object beforeCache(Object obj, boolean isTopObject) {
		if (isTopObject) {
			return preProcessCacheObject ? ZipUtil.compress(XmlSerializationUtil.xStreamSerialize(obj).getBytes()) : obj;
		} else {
			return preProcessCacheObject ? DatatypeConverter.printHexBinary(XmlSerializationUtil.xStreamSerialize(obj).getBytes())
					: obj;
		}
	}

	public synchronized Object afterCache(Object obj, boolean isTopObject) {
		if (isTopObject) {
			return preProcessCacheObject ? XmlSerializationUtil.xStreamDeserialize(new String(ZipUtil.decompress((byte[])obj)))
					: obj;
		} else {
			return preProcessCacheObject ? XmlSerializationUtil.xStreamDeserialize(new String(
					DatatypeConverter.parseHexBinary((String)obj))) : obj;
		}
	}

	private void lock(String key) {
		try {
			lock.acquire(1);
			Semaphore semaphore = requests.get(key);
			if (semaphore == null) {
				semaphore = new Semaphore(1);
				requests.put(key, semaphore);
				lock.release(1);
				semaphore.acquire(1);
			} else {
				lock.release(1);
				semaphore.tryAcquire(1, semaphoreWaitTimeOut, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unlock(String key) {
		try {
			unlock.acquire(1);
			Semaphore semaphore = requests.get(key);
			if (semaphore != null) {
				if (!semaphore.hasQueuedThreads()) {
					requests.remove(key);
				}
				semaphore.release(1);
			}
			unlock.release(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateDuration(String keyPart, WebServiceMethodDefinition wsMDef, long newDuration) {
		List<String> keys = cacheEngine.getKeys(keyPart);
		List<UpdateDuration> updateDuration = new ArrayList<UpdateDuration>();
		for (String key : keys) {
			updateDuration.add(new UpdateDuration(key, wsMDef.getCacheDuration(), newDuration));
		}
		try {
			ThreadWorkSeparatorUtil.newInstance().start(updateDuration, OnUpdateRun.class);
			((SimpleWebServiceMethodDefinition)wsMDef).setCacheDuration(newDuration);
		} catch (Exception e) {
			lastError = WebServiceCacheError.CACHE_ERROR;
		}

		blockedLocally = false;
	}

	@Override
	public void updateCacheDuration(String serviceName, String methodname, long newDuration) {
		blockedLocally = true;

		WebServiceMethodDefinition wsMDef = null;
		try {
			wsMDef = wsRegistry.getWebServiceByName(serviceName).getMethodByName(methodname);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (wsMDef == null) {
			blockedLocally = false;
			return;
		}
		addBackGroundOperation(UPDATE, String.format("%s.%s", serviceName, methodname), wsMDef, newDuration);
	}

	private synchronized boolean hasErrors() {
		if (lastError == WebServiceCacheError.ALL_OK) {
			lastError = cacheEngine.getLastError();
		}
		return lastError != WebServiceCacheError.ALL_OK;
	}

}
