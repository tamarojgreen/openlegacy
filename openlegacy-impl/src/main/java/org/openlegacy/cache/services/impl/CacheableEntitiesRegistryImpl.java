package org.openlegacy.cache.services.impl;

import org.openlegacy.SessionAction;
import org.openlegacy.cache.services.CacheableEntitiesRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheableEntitiesRegistryImpl implements CacheableEntitiesRegistry {

	private static class Entry {

		public int expiry;
		public List<Class<?>> getActions;
		public List<Class<?>> putActions;
		public List<Class<?>> removeActions;
	}

	Map<Class<?>, Entry> entries = new HashMap<Class<?>, CacheableEntitiesRegistryImpl.Entry>();

	@Override
	public synchronized void addEntity(Class<?> entityClass, int expiry, Class<?>[] getActions,
			Class<?>[] putActions, Class<?>[] removeActions) {
		if (!entries.containsKey(entityClass)) {
			Entry entry = new Entry();
			entry.expiry = expiry;
			entry.getActions = new ArrayList<Class<?>>(Arrays.asList(getActions));
			entry.putActions = new ArrayList<Class<?>>(Arrays.asList(putActions));
			entry.removeActions= new ArrayList<Class<?>>(Arrays.asList(removeActions));

			entries.put(entityClass, entry);
		}
	}

	@Override
	public synchronized void removeEntity(Class<?> entityClass) {
		entries.remove(entityClass);
	}

	@Override
	public synchronized boolean isCacheable(Class<?> entityClass) {
		return entries.containsKey(entityClass);
	}

	@Override
	public synchronized int getEntityExpiry(Class<?> entityClass) {
		return entries.containsKey(entityClass) ? entries.get(entityClass).expiry : -1;
	}

	@Override
	public boolean isGetAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass) {
		Entry entry = entries.get(entityClass);
		if (entry != null) {
			return entry.getActions.contains(actionClass);
		}

		return false;
	}

	@Override
	public boolean isPutAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass) {
		Entry entry = entries.get(entityClass);
		if (entry != null) {
			return entry.putActions.contains(actionClass);
		}

		return false;
	}

	@Override
	public boolean isRemoveAction(Class<?> entityClass, Class<? extends SessionAction<?>> actionClass) {
		Entry entry = entries.get(entityClass);
		if (entry != null) {
			return entry.removeActions.contains(actionClass);
		}

		return false;
	}

}
