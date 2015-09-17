package org.openlegacy.cache.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheManager;
import org.openlegacy.cache.ehcache.EhcacheCacheManager;
import org.openlegacy.cache.memcached.MemcachedCacheManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CacheITCase {

	private static final String generateCacheName() {
		return CacheITCase.class.getSimpleName() + "_" + Long.toHexString(Calendar.getInstance().getTimeInMillis());
	}

	private static final String generateKey() {
		return "key_" + Long.toHexString(Calendar.getInstance().getTimeInMillis());
	}

	private CacheManager cacheManager;

	@Parameters(name = "{index} => {0}")
	public static Collection<?> data() {
		List<Object[]> cacheManagers = new ArrayList<Object[]>();

		// ehcache

		EhcacheCacheManager ecm = new EhcacheCacheManager();
		cacheManagers.add(new Object[] { ecm });

		// memcached

		MemcachedCacheManager mcm = new MemcachedCacheManager();
		mcm.setAddresses("127.0.0.1:" + System.getProperty("memcached.port"));
		cacheManagers.add(new Object[] { mcm });

		return cacheManagers;
	}

	public CacheITCase(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	Cache<String, String> cache;

	@Before
	public void before() {
		String cacheName = generateCacheName();

		// check if cache doesn't exist
		Assert.assertNull(cacheManager.getCache(cacheName));

		// create new cache
		cache = cacheManager.createCache(cacheName);
		Assert.assertNotNull(cache);
		Assert.assertEquals(cache.getName(), cacheName);
		Assert.assertEquals(cache, cacheManager.getCache(cacheName));
	}

	@Test
	public void basicCacheOperationsTest() {
		String key = generateKey();
		String value = "value";

		// check if key-value don't exists in cache
		Assert.assertNull(cache.get(key));

		// put the value and check if it exists
		cache.put(key, value);
		Assert.assertEquals(value, cache.get(key));

		// remove the value
		cache.remove(key);
		Assert.assertNull(cache.get(key));
	}

	@Test
	public void expiryTest() throws InterruptedException {
		String key = generateKey();
		String value = "value";

		int expiryInSeconds = 2;
		cache.put(key, value, expiryInSeconds);

		// wait half of tti and check for value
		Thread.sleep(expiryInSeconds * 1000 / 2);
		Assert.assertNotNull(cache.get(key));

		// wait more and check for value
		Thread.sleep((expiryInSeconds + 1) * 1000);
		Assert.assertNull(cache.get(key));
	}

	@After
	public void after() {
		// drop created cache
		cacheManager.destroyCache(cache.getName());
		Assert.assertNull(cacheManager.getCache(cache.getName()));
	}
}
