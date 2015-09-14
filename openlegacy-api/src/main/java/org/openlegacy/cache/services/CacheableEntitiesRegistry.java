package org.openlegacy.cache.services;

import org.openlegacy.SessionAction;
import org.openlegacy.cache.CacheInfo;

import java.util.List;

public interface CacheableEntitiesRegistry {

	public static int DEFAULT_EXPIRY = -1;

	void addEntity(Class<?> entityClass, int expiry, Class<?>[] getActions, Class<?>[] putActions,
			Class<?>[] removeActions);

	void removeEntity(Class<?> entityClass);

	boolean isCacheable(Class<?> entityClass);

	int getDefaultEntityExpiry(Class<?> entityClass);

	int getEntityExpiry(Class<?> entityClass);

	void setEntityExpiry(Class<?> entityClass, int expiry);

	boolean isGetAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass);

	boolean isPutAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass);

	boolean isRemoveAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass);

	List<CacheInfo> getStats();

	public String getEntityCacheName(String entityClassName);
}
