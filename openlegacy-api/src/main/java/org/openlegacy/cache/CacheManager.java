package org.openlegacy.cache;

import java.util.List;

public interface CacheManager {

	<K, V> Cache<K, V> createCache(String cacheName);

	<K, V> Cache<K, V> getCache(String cacheName);

	List<CacheInfo> getCacheStats();

	void destroyCache(String cacheName);
}
