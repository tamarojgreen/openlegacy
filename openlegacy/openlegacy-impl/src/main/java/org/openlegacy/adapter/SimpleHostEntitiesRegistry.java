package org.openlegacy.adapter;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.HostEntityDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation for host entities registry
 * 
 */
public abstract class SimpleHostEntitiesRegistry<D extends HostEntityDefinition> implements HostEntitiesRegistry<D> {

	private final Map<Class<?>, D> entityDefinitions = new HashMap<Class<?>, D>();

	@SuppressWarnings("unchecked")
	private Map<String, Class<?>> hostEntities = new CaseInsensitiveMap();
	private Map<Class<?>, String> reversedHostEntities = new HashMap<Class<?>, String>();

	private void add(String screenName, Class<?> hostEntity) {
		hostEntities.put(screenName, hostEntity);
		reversedHostEntities.put(hostEntity, screenName);
	}

	public List<Class<?>> getAll() {
		return (List<Class<?>>)hostEntities.values();
	}

	public Class<?> getEntityClass(String screenName) {
		return hostEntities.get(screenName);
	}

	public String getEntityName(Class<?> hostEntity) {
		return reversedHostEntities.get(hostEntity);
	}

	protected Map<String, Class<?>> getHostEntities() {
		return hostEntities;
	}

	public void add(D screenEntityDefinition) {
		add(screenEntityDefinition.getHostEntityName(), screenEntityDefinition.getHostEntityClass());
		getEntityDefinitions().put(screenEntityDefinition.getHostEntityClass(), screenEntityDefinition);
	}

	public D get(Class<?> entityClass) {
		return getEntityDefinitions().get(entityClass);
	}

	public Map<Class<?>, D> getEntityDefinitions() {
		return entityDefinitions;
	}
}
