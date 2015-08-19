package org.openlegacy.cache.modules;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.SessionAction;
import org.openlegacy.cache.Cache;
import org.openlegacy.cache.CacheManager;
import org.openlegacy.cache.DummyCacheManager;
import org.openlegacy.cache.services.CacheableEntitiesRegistry;
import org.openlegacy.support.SessionModuleAdapter;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("rawtypes")
public class CacheModule extends SessionModuleAdapter {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(CacheModule.class);

	@Inject
	private CacheManager cacheManager;

	@Inject
	private CacheableEntitiesRegistry cacheableEntitiesRegistry;

	@Value("${openlegacy.cache.enabled:false}")
	private boolean enabled;

	@Value("${openlegacy.cache.slowquery:0}")
	private int slowQuery;

	List<String> cacheNames = new ArrayList<String>();

	public static interface ObtainEntityCallback {

		List<Object> getEntityKeys();

		Object obtainEntity();
	}

	private static void slowQuery(long seconds) {
		try {
			logger.debug("******************* S L O W  Q U E R Y *******************");
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	private String getCacheName(Class<?> entityClass) {
		return Integer.toHexString(cacheManager.hashCode()) + ":" + entityClass.getSimpleName();
	}

	private Cache<String, String> getCache(Class<?> entityClass) {
		if (!getCacheableEntitiesRegistry().isCacheable(entityClass)) {
			return null;
		}

		String cacheName = getCacheName(entityClass);

		Cache<String, String> cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			cacheNames.add(cacheName);
			cache = cacheManager.createCache(cacheName);
			cache.setExpiry(cacheableEntitiesRegistry.getEntityExpiry(entityClass));
			if (cache != null) {
				logger.debug("Created cache with name \"" + cacheName + "\"");
			}
		}

		return cache;
	}

	private static String getCacheKeyName(Class<?> entityClass, List<Object> keys) {
		// build key for an entity
		StringBuilder sb = new StringBuilder("(").append(entityClass.getSimpleName());
		for (Object k : keys) {
			sb.append(",").append(k.toString());
		}
		sb.append(")");

		return sb.toString();
	}

	public Cache<String, String> getCache(Object entity) {
		return getCache(entity.getClass());
	}

	public String getCacheName(Object entity) {
		return getCacheName(entity.getClass());
	}

	public String getCacheKeyName(Object entity, List<Object> keys) {
		return getCacheKeyName(entity.getClass(), keys);
	}

	public Object doStuff(Class<? extends SessionAction<?>> actionClass, Class<?> entityClass,
			ObtainEntityCallback callback) {
		return doStuff(actionClass, entityClass, callback, false);
	}

	public Object doStuff(Class<? extends SessionAction<?>> actionClass, Class<?> entityClass,
			ObtainEntityCallback callback, boolean emptyActionIsGet) {
		if (callback == null) {
			return null;
		}
		Object entity = null;

		if (cacheManager instanceof DummyCacheManager || !isEnabled()) {
			return callback.obtainEntity();
		}

		boolean shouldUpdateCacheValue = true;
		String key = null;
		String value = null;

		Cache<String, String> cache = getCache(entityClass);

		// no need to update cache if it's null
		shouldUpdateCacheValue &= (cache != null);

		if (cache != null) {
			key = getCacheKeyName(entityClass, callback.getEntityKeys());

			if (!getCacheableEntitiesRegistry().isPutAction(entityClass, actionClass)) {
				if (getCacheableEntitiesRegistry().isGetAction(entityClass, actionClass)
						|| (actionClass == null && emptyActionIsGet)) {
					value = cache.get(key);

					if (value != null) {
						logger.debug(key + " found in cache " + getCacheName(entityClass));
						try {
							entity = getEntityFromJson(entityClass, value);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (entity != null) {
							shouldUpdateCacheValue = false;
						}
					}
				} else {
					shouldUpdateCacheValue = false;
				}

				if (getCacheableEntitiesRegistry().isRemoveAction(entityClass, actionClass)) {
					cache.remove(key);

					logger.debug(key + " removed from cache " + getCacheName(entityClass));

					shouldUpdateCacheValue = false;
				}
			}
		}

		if (entity == null) {
			if (slowQuery > 0) {
				slowQuery(2000);
			}

			entity = callback.obtainEntity();

			if (shouldUpdateCacheValue) {
				try {
					value = getJsonFromEntity(entity);
				} catch (JsonProcessingException e) {
					value = null;
					e.printStackTrace();
				}

				if (value != null) {
					cache.put(key, value);

					logger.debug(key + " added to cache " + getCacheName(entity));
					// TODO: remove this
					logger.debug(value);
				}
			}
		}

		return entity;
	}

	private static String getJsonFromEntity(Object entity) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		VisibilityChecker<?> vc =
				mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE);
		mapper.setVisibility(vc);
		return mapper.writeValueAsString(entity);
	}

	private static Object getEntityFromJson(Class<?> entityClass, String value) throws JsonParseException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		VisibilityChecker<?> vc =
				mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE);
		mapper.setVisibility(vc);
		return mapper.readValue(value, entityClass);
	}

	public void dropAllCaches() {
		for (String cacheName : cacheNames) {
			cacheManager.destroyCache(cacheName);
			logger.debug("Destroyed cache with name \"" + cacheName + "\"");
		}

		cacheNames.clear();
	}

	public CacheableEntitiesRegistry getCacheableEntitiesRegistry() {
		return cacheableEntitiesRegistry;
	}

	public void setCacheableEntitiesRegistry(CacheableEntitiesRegistry cacheableEntitiesRegistry) {
		this.cacheableEntitiesRegistry = cacheableEntitiesRegistry;
	}

	@Override
	public void destroy() {}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
