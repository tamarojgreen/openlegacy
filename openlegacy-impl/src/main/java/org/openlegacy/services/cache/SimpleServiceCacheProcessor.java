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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.ServicesRegistry;
import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheInfo;
import org.openlegacy.cache.CacheManager;
import org.openlegacy.services.definitions.ServiceDefinition;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.openlegacy.utils.ThreadWorkSeparatorUtil.OnRun;
import org.openlegacy.utils.XmlSerializationUtil;
import org.openlegacy.utils.ZipUtil;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
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

	private final static Log logger = LogFactory.getLog(SimpleServiceCacheProcessor.class);

	private static final int ADD = 0;
	private static final int REMOVE = 1;
	private static final int UPDATE = 2;
	private static final int CLEAR = 3;

	public static SimpleServiceCacheProcessor INSTANCE;

	private CacheManager cacheManager;

	@Inject
	private ServicesRegistry wsRegistry;

	@Inject
	private ApplicationContext applicationContext;

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

		@SuppressWarnings("unused")
		public UpdateDuration(String key, long oldDuration, long newDuration) {
			this.key = key;
			this.oldDuration = oldDuration;
			this.newDuration = newDuration;
		}
	}

	public static class OnUpdateRun implements OnRun {

		SimpleServiceCacheProcessor wsCacheProcessor = SimpleServiceCacheProcessor.INSTANCE;

		@Override
		public void onRun(Object obj) {}
	}

	private volatile BlockingQueue<BackGroundOperation> backGroundWork = new LinkedBlockingDeque<BackGroundOperation>();
	private volatile Semaphore lock = new Semaphore(1, true), unlock = new Semaphore(1, true);
	private volatile Map<String, Semaphore> requests = new LinkedHashMap<String, Semaphore>();

	private volatile int lastError = ServiceCacheError.NULL;

	private boolean preProcessCacheObject;
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
					break;
				case CLEAR:
					if (data.length == 0) {
						clearCache(null, false);
					} else {
						clearCache((String)data[0], (Boolean)data[1]);
					}
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
			lastError = ServiceCacheError.ALL_OK;
		} catch (Exception e) {
			lastError = ServiceCacheError.CACHE_INIT_ERROR;
		}
		new Thread(backGroundWorker).start();
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method origin = invocation.getMethod();

		if (cacheManager == null) {
			cacheManager = (CacheManager)applicationContext.getBean("cacheManager");
			if (cacheManager == null) {
				lastError = ServiceCacheError.ENGINE_INIT_ERROR;
				return invocation.proceed();
			}
		}

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
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Key %s was locked", key));
			}
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
			getCache(getCacheNameForKey(key)).put(key, beforeCache(cacheData, true),
					(int)(methodDefinition.getCacheDuration() / 1000));
			unlock(key);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Key %s put in cache", key));
			}
		} catch (Exception e) {
			lastError = ServiceCacheError.CACHE_ERROR;
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Cache<String, Object> getCache(String name) {
		Cache<String, Object> result = cacheManager.getCache(name);
		return (Cache<String, Object>)(result == null ? cacheManager.createCache(name) : result);
	}

	private String getCacheNameForKey(String key) {
		return key.split("\\.")[0] + "Service";
	}

	@Override
	public Object get(String key, long accessTime) {
		if (hasErrors()) {
			return null;
		}
		try {
			Object cached = getCache(getCacheNameForKey(key)).get(key);
			if (cached == null) {
				return null;
			}

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
		getCache(getCacheNameForKey(key)).remove(key);
		blockedKeys.remove(key);
	}

	@Override
	public void update(String key, Object obj) {}

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
	public void destroy() {
		onDestroy = true;
	}

	@Override
	public int getLastError() {
		return lastError;
	}

	@Override
	public void fix() {
		if (lastError == ServiceCacheError.ENGINE_INIT_ERROR && cacheManager != null) {
			lastError = ServiceCacheError.ALL_OK;
		}
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
		addBackGroundOperation(CLEAR, String.format("%s.%s", serviceName, methodName), false);
		if (wsMDef instanceof SimpleServiceMethodDefinition) {
			((SimpleServiceMethodDefinition)wsMDef).setCacheDuration(newDuration);
		}
	}

	private synchronized boolean hasErrors() {
		return lastError != ServiceCacheError.ALL_OK;
	}

	private synchronized boolean isKeyBlocked(String key) {
		return blockedKeys.contains(key);
	}

	private List<String> getKeys(List<String> keys, String mask) {
		List<String> result = new ArrayList<String>();
		for (String value : keys) {
			if (value.contains(mask)) {
				result.add(value);
			}
		}
		return result;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void clearCache(String keyMask, boolean clearCache) {
		if (keyMask == null) {
			for (CacheInfo<String> info : cacheManager.getCacheStats()) {
				blockedKeys.addAll(info.getKeys());
				cacheManager.getCache(info.getName()).clear();
				blockedKeys.removeAll(info.getKeys());
			}
		} else {
			CacheInfo<String> info = getCache(getCacheNameForKey(keyMask)).getInfo();
			if (clearCache) {
				blockedKeys.addAll(info.getKeys());
				cacheManager.getCache(info.getName()).clear();
				blockedKeys.removeAll(info.getKeys());
			} else {
				List<String> removeList = getKeys(info.getKeys(), keyMask);
				blockedKeys.addAll(removeList);
				cacheManager.getCache(info.getName()).removeAll(new HashSet<String>(removeList));
				blockedKeys.removeAll(removeList);
			}
		}
	}

	@Override
	public void clear() {
		addBackGroundOperation(CLEAR);

	}

	@Override
	public void clear(String keyMask, boolean clearCache) {
		addBackGroundOperation(CLEAR, keyMask, clearCache);
	}
}
