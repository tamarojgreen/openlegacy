package org.openlegacy.cache;

import java.util.Set;

public interface Cache<K, V> {

	V get(K key);

	void put(K key, V value);

	void put(K key, V value, int expiry);

	void remove(K key);

	void removeAll(Set<? extends K> keys);

	void removeAll();

	void clear();

	String getName();

	CacheInfo getInfo();

	void setExpiry(int expiry);

	int getExpiry();

}
