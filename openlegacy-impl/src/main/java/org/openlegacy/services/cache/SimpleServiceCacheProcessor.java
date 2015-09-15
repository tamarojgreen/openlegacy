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

package org.openlegacy.services.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openlegacy.ServicesRegistry;
import org.openlegacy.services.definitions.ServiceDefinition;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.openlegacy.utils.ThreadWorkSeparatorUtil.OnRun;
import org.openlegacy.utils.XmlSerializationUtil;
import org.openlegacy.utils.ZipUtil;

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

public class SimpleServiceCacheProcessor implements ServiceCacheProcessor, MethodInterceptor {

	private static final int ADD = 0;
	private static final int REMOVE = 1;
	private static final int UPDATE = 2;

	public static SimpleServiceCacheProcessor INSTANCE;

	@Inject
	private ServicesRegistry wsRegistry;

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

		SimpleServiceCacheProcessor wsCacheProcessor = SimpleServiceCacheProcessor.INSTANCE;

		@Override
		public void onRun(Object obj) {
			UpdateDuration ud = (UpdateDuration)obj;
			ServiceCacheEngine wsCacheEngine = wsCacheProcessor.getCacheEngine();
			Object cached = wsCacheEngine.get(ud.key, -1);
			if (cached == null) {
				return;
			}
			ServiceCacheData cacheData = (ServiceCacheData)wsCacheProcessor.afterCache(cached, true);
			cacheData.setExpirationTime(cacheData.getExpirationTime() - ud.oldDuration + ud.newDuration);
			cached = wsCacheProcessor.beforeCache(cacheData, true);
			wsCacheEngine.update(ud.key, cached);
		}
	}

	private volatile BlockingQueue<BackGroundOperation> backGroundWork = new LinkedBlockingDeque<BackGroundOperation>();
	private volatile Semaphore lock = new Semaphore(1, true), unlock = new Semaphore(1, true);
	private volatile Map<String, Semaphore> requests = new LinkedHashMap<String, Semaphore>();

	private volatile int lastError = ServiceCacheError.NULL;

	private boolean preProcessCacheObject;
	private ServiceCacheEngine cacheEngine;
	private int semaphoreWaitTimeOut = 10;
	private MessageDigest messageDigest;

	private List<String> blockedKeys = new ArrayList<String>();

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
				if (!backGroundWork.isEmpty()) {
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
					put((String)data[0], data[1], (ServiceMethodDefinition)data[2]);
					break;
				case REMOVE:
					remove((String)data[0]);
					break;
				case UPDATE:
					updateDuration((String)data[0], (ServiceMethodDefinition)data[1], (Long)data[2]);
					break;
				default:
					break;
			}
		}
	};
	private volatile boolean onDestroy = false;

	public SimpleServiceCacheProcessor(boolean preProcessCacheObject) {
		this.preProcessCacheObject = preProcessCacheObject;
		SimpleServiceCacheProcessor.INSTANCE = this;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			lastError = ServiceCacheError.CACHE_INIT_ERROR;
		}
		new Thread(backGroundWorker).start();
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method origin = invocation.getMethod();

		if (origin.getReturnType() == void.class) {
			return invocation.proceed();
		}

		ServiceDefinition wsDef = wsRegistry.getServiceByClass(origin.getDeclaringClass());

		if (wsDef == null) {
			return invocation.proceed();
		}

		ServiceMethodDefinition wsMDef = wsDef.getMethodByName(origin.getName());

		if (wsMDef == null || wsMDef.getCacheDuration() == 0) {
			return invocation.proceed();
		}

		String key = String.format("%s.%s.%s", wsDef.getName(), wsMDef.getName(), generateKey(invocation.getArguments()));

		long accessTime = System.currentTimeMillis();

		lock(key);

		if (isKeyBlocked(key)) {
			return invocation.proceed();
		}

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
	public void put(String key, Object obj, ServiceMethodDefinition methodDefinition) {
		if (hasErrors()) {
			return;
		}
		try {
			ServiceCacheData cacheData = new ServiceCacheData();
			cacheData.setData(beforeCache(obj, false));
			cacheData.setExpirationTime(System.currentTimeMillis() + methodDefinition.getCacheDuration());
			cacheEngine.put(key, beforeCache(cacheData, true));
			unlock(key);
		} catch (Exception e) {
			lastError = ServiceCacheError.CACHE_ERROR;
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
			ServiceCacheData cacheData = (ServiceCacheData)afterCache(cached, true);
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
			lastError = ServiceCacheError.CACHE_ERROR;
			return null;
		}
	}

	@Override
	public void remove(String key) {
		if (hasErrors()) {
			return;
		}
		cacheEngine.remove(key);
		blockedKeys.remove(key);
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
	public void setEngine(ServiceCacheEngine cacheEngine) {
		try {
			this.cacheEngine = cacheEngine;
			if (!cacheEngine.init()) {
				lastError = ServiceCacheError.ENGINE_INIT_ERROR;
			} else {
				lastError = ServiceCacheError.ALL_OK;
			}
		} catch (Exception e) {
			lastError = ServiceCacheError.ENGINE_INIT_ERROR;
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
	public void fixEngine() {
		cacheEngine.fix();
	}

	public ServiceCacheEngine getCacheEngine() {
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

	private void updateDuration(String keyPart, ServiceMethodDefinition wsMDef, long newDuration) {
		List<String> keys = cacheEngine.getKeys(keyPart);
		for (String key : keys) {
			blockedKeys.add(key);
			addBackGroundOperation(REMOVE, key);
		}
		/*
		 * this code produced such logic 1. Get all "equal" keys from engine. 2. Separate records to each processor unit. 3.
		 * Update cached record(decompress -> deserialize -> update -> serialize -> compress). 4. Update record in cache.
		 * 
		 * As far as on cache duration changing I must delete cached records - I comment this call, if need - uncomment it
		 */
		// List<UpdateDuration> updateDuration = new ArrayList<UpdateDuration>();
		// for (String key : keys) {
		// updateDuration.add(new UpdateDuration(key, wsMDef.getCacheDuration(), newDuration));
		// }
		// try {
		// ThreadWorkSeparatorUtil.newInstance().start(updateDuration, OnUpdateRun.class);
		// ((SimpleServiceMethodDefinition)wsMDef).setCacheDuration(newDuration);
		// } catch (Exception e) {
		// lastError = ServiceCacheError.CACHE_ERROR;
		// }

	}

	@Override
	public void updateCacheDuration(String serviceName, String methodName, long newDuration) {
		ServiceMethodDefinition wsMDef = null;
		try {
			wsMDef = wsRegistry.getServiceByName(serviceName).getMethodByName(methodName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (wsMDef == null) {
			return;
		}
		addBackGroundOperation(UPDATE, String.format("%s.%s", serviceName, methodName), wsMDef, newDuration);
		// if you will uncomment upper comment - comment this condition
		if (wsMDef instanceof SimpleServiceMethodDefinition) {
			((SimpleServiceMethodDefinition)wsMDef).setCacheDuration(newDuration);
		}
	}

	private synchronized boolean hasErrors() {
		if (lastError == ServiceCacheError.ALL_OK) {
			lastError = cacheEngine.getLastError();
		}
		return lastError != ServiceCacheError.ALL_OK;
	}

	private synchronized boolean isKeyBlocked(String key) {
		return blockedKeys.contains(key);
	}

}
