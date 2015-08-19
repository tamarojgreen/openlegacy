package org.openlegacy.cache.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheInfo;
import org.openlegacy.cache.CacheManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemcachedCacheManager implements CacheManager {

	MemcachedClient client = null;
	Map<String, Cache<?, ?>> knownCaches = new HashMap<String, Cache<?, ?>>();
	private String addresses;

	@Override
	public synchronized <K, V> Cache<K, V> createCache(String cacheName) {

		Cache<K, V> cache = null;

		if (client == null && addresses != null && !addresses.isEmpty()) {
			try {
				client = new MemcachedClient(AddrUtil.getAddresses(addresses));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (client != null) {
			cache = new MemcachedCache<K, V>(cacheName, client);
			knownCaches.put(cacheName, cache);
		}

		return cache;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <K, V> Cache<K, V> getCache(String cacheName) {
		return (Cache<K, V>) knownCaches.get(cacheName);
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
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

}
