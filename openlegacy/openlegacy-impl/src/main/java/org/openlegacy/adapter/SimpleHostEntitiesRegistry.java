package org.openlegacy.adapter;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.openlegacy.HostEntitiesRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation for host entities registry
 * 
 */
public class SimpleHostEntitiesRegistry implements HostEntitiesRegistry {

	@SuppressWarnings("unchecked")
	private Map<String, Class<?>> hostEntities = new CaseInsensitiveMap();
	private Map<Class<?>, String> reversedHostEntities = new HashMap<Class<?>, String>();

	public void add(String screenName, Class<?> screenModel) {
		hostEntities.put(screenName, screenModel);
		reversedHostEntities.put(screenModel, screenName);
	}

	public List<Class<?>> getAll() {
		return (List<Class<?>>)hostEntities.values();
	}

	public Class<?> get(String screenName) {
		return hostEntities.get(screenName);
	}

	protected Map<String, Class<?>> getHostEntities() {
		return hostEntities;
	}

	public String get(Class<?> hostEntity) {
		return reversedHostEntities.get(hostEntity);
	}
}
