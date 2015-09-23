package org.openlegacy.cache.ehcache;

import net.sf.ehcache.Element;

import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheInfo;

import java.util.Set;

public class EhcacheCache<K, V> implements Cache<K, V> {

	net.sf.ehcache.Cache cache;

	public EhcacheCache(net.sf.ehcache.Cache cache) {
		this.cache = cache;
	}

	@Override
	public V get(K key) {
		Element e = cache.get(key);
		return (e != null) ? (V)e.getObjectValue() : null;
	}

	@Override
	public void put(K key, V value) {
		put(key, value, getExpiry());
	}

	@Override
	public void put(K key, V value, int expiry) {
		cache.put(new Element(key, value, expiry, 0));
	}

	@Override
	public void remove(K key) {
		cache.remove(key);
	}

	@Override
	public void removeAll(Set<? extends K> keys) {
		cache.removeAll(keys);
	}

	@Override
	public void removeAll() {
		cache.removeAll();
	}

	@Override
	public void clear() {
		cache.removeAll(true);
	}

	@Override
	public String getName() {
		return cache.getName();
	}

	@Override
	public CacheInfo getInfo() {
		CacheInfo ci = new CacheInfo();
		ci.setName(cache.getName());
		ci.setCurrentExpiry(cache.getCacheConfiguration().getTimeToIdleSeconds());
		ci.setElementsCount(cache.getSize());
		ci.setKeys(cache.getKeys());

		return ci;
	}

	@Override
	public void setExpiry(int expiry) {
		cache.getCacheConfiguration().setTimeToIdleSeconds(expiry);
	}

	@Override
	public int getExpiry() {
		return (int)cache.getCacheConfiguration().getTimeToIdleSeconds();
	}

}
