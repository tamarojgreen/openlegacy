package org.openlegacy.cache;

import java.util.List;


public class DummyCacheManager implements CacheManager {

	@Override
	public <K, V> Cache<K, V> createCache(String cacheName) {
		return null;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) {
		return null;
	}

	@Override
	public List<CacheInfo> getCacheStats() {
		return null;
	}

	@Override
	public void destroyCache(String cacheName) {
		// TODO Auto-generated method stub

	}

}
