package org.openlegacy.support;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.openlegacy.FieldDefinition;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.HostEntityDefinition;
import org.openlegacy.HostEntityType;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation for host entities registry
 * 
 */
public abstract class AbstractHostEntitiesRegistry<H extends HostEntityDefinition<D>, D extends FieldDefinition> implements HostEntitiesRegistry<H, D> {

	private final Map<Class<?>, H> entitiesDefinitions = new HashMap<Class<?>, H>();

	@SuppressWarnings("unchecked")
	private Map<String, Class<?>> hostEntities = new CaseInsensitiveMap();
	private Map<Class<?>, String> reversedHostEntities = new HashMap<Class<?>, String>();

	// map of entities by type
	private Map<Class<? extends HostEntityType>, List<Class<?>>> entitiesByTypes = new HashMap<Class<? extends HostEntityType>, List<Class<?>>>();

	private void add(String entityName, Class<?> hostEntity) {
		hostEntities.put(entityName, hostEntity);
		reversedHostEntities.put(hostEntity, entityName);
	}

	protected void addToTypes(Class<? extends HostEntityType> hostEntityType, Class<?> hostEntity) {
		List<Class<?>> relevantEntities = entitiesByTypes.get(hostEntityType);
		if (relevantEntities == null) {
			relevantEntities = new ArrayList<Class<?>>();
			entitiesByTypes.put(hostEntityType, relevantEntities);
		}
		relevantEntities.add(hostEntity);

	}

	public List<Class<?>> getAll() {
		return (List<Class<?>>)hostEntities.values();
	}

	public Class<?> getEntityClass(String entityName) {
		return hostEntities.get(entityName);
	}

	public String getEntityName(Class<?> hostEntity) {
		return reversedHostEntities.get(hostEntity);
	}

	protected Map<String, Class<?>> getHostEntities() {
		return hostEntities;
	}

	public void add(H hostEntityDefinition) {
		add(hostEntityDefinition.getEntityName(), hostEntityDefinition.getEntityClass());
		addToTypes(hostEntityDefinition.getType(), hostEntityDefinition.getEntityClass());
		getEntitiesDefinitions().put(hostEntityDefinition.getEntityClass(), hostEntityDefinition);
	}

	public H get(Class<?> entityClass) {
		entityClass = ProxyUtil.getOriginalClass(entityClass);
		return getEntitiesDefinitions().get(entityClass);
	}

	public Map<Class<?>, H> getEntitiesDefinitions() {
		return entitiesDefinitions;
	}

	public List<Class<?>> getByType(Class<? extends HostEntityType> hostEntityType) {
		return entitiesByTypes.get(hostEntityType);
	}

	public H getFirstEntityDefinition(Class<? extends HostEntityType> hostEntityType) throws RegistryException {
		List<Class<?>> matchingTypes = getByType(hostEntityType);

		if (matchingTypes.size() == 0) {
			throw (new RegistryException(MessageFormat.format("Host entity {0}not defined in the registry", hostEntityType)));
		}

		if (matchingTypes.size() > 1) {
			throw (new RegistryException(MessageFormat.format("Found {0} matching host entities in the registry",
					matchingTypes.size())));
		}

		return get(matchingTypes.get(0));
	}

}
