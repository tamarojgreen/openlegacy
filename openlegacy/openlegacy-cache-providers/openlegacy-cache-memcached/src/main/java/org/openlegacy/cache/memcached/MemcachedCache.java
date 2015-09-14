package org.openlegacy.cache.memcached;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheInfo;

import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MemcachedCache<K, V> implements Cache<K, V> {

	String cacheName;
	MemcachedClient client;
	private int expiry = 3600;
	private AtomicInteger elementsCount = new AtomicInteger(0);

	private static AtomicInteger CACHE_ID = new AtomicInteger(0);

	private String makeKey(K key) {
		return Integer.toString(CACHE_ID.get()) + ":" + cacheName + ":" + key.toString();
	}

	public MemcachedCache(String cacheName, MemcachedClient client) {
		this.client = client;
		this.cacheName = cacheName;

		CACHE_ID.incrementAndGet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		V v = null;
		Future<Object> f = client.asyncGet(makeKey(key));
		try {
			v = (V) f.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			f.cancel(true);
		}
		return v;
	}

	@Override
	public void put(K key, V value) {
		put(key, value, expiry);
	}

	@Override
	public void put(K key, V value, int expiry) {
		OperationFuture<Boolean> f = client.set(makeKey(key), expiry, value);
		try {
			f.get();
			elementsCount.incrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
			f.cancel();
		}
	}

	@Override
	public void remove(K key) {
		OperationFuture<Boolean> f = client.delete(makeKey(key));
		try {
			f.get();
			elementsCount.decrementAndGet();
		} catch (Exception e) {
			e.printStackTrace();
			f.cancel();
		}
	}

	@Override
	public void removeAll(Set<? extends K> keys) {
		for (K key : keys) {
			remove(key);
		}
	}

	@Override
	public void removeAll() {
		CACHE_ID.incrementAndGet();
		elementsCount.set(0);
	}

	@Override
	public void clear() {
		CACHE_ID.incrementAndGet();
		elementsCount.set(0);
	}

	@Override
	public String getName() {
		return cacheName;
	}

	@Override
	public CacheInfo getInfo() {
		CacheInfo ci = new CacheInfo();
		ci.setName(cacheName);
		ci.setDefaultExpiry(expiry);
		ci.setElementsCount(elementsCount.intValue());

		return ci;
	}

	@Override
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	@Override
	public int getExpiry() {
		return expiry;
	}

}
