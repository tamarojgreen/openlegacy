package org.openlegacy.cache.ehcache;

import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheInfo;
import org.openlegacy.cache.CacheManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EhcacheCacheManager implements CacheManager {

	net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance();

	Map<String, Cache<?, ?>> knownCaches = new HashMap<String, Cache<?, ?>>();

	@Override
	public synchronized <K, V> Cache<K, V> createCache(String cacheName) throws IllegalArgumentException {
		if (cacheManager.cacheExists(cacheName)) {
			throw new IllegalArgumentException("Cache with name \"" + cacheName + "\" already exists");
		}
		cacheManager.addCache(cacheName);
		Cache<K, V> cache = getCache(cacheName);
		if (cache != null) {
			knownCaches.put(cacheName, cache);
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) {
		if (knownCaches.containsKey(cacheName)) {
			return (Cache<K, V>) knownCaches.get(cacheName);
		}
		net.sf.ehcache.Cache cache = cacheManager.getCache(cacheName);
		return (cache != null) ? new EhcacheCache<K, V>(cache) : null;
	}

	@Override
	public List<CacheInfo> getCacheStats() {
		List<CacheInfo> l = new ArrayList<CacheInfo>();
		for (String cacheName : knownCaches.keySet()) {
			l.add(knownCaches.get(cacheName).getInfo());
		}
		return l;
	}

	@Override
	public synchronized void destroyCache(String cacheName) {
		knownCaches.remove(cacheName);
		cacheManager.removeCache(cacheName);
	}

}
