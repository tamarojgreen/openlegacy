package org.openlegacy.support;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.FieldDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract implementation of entities registry
 * 
 */
public abstract class AbstractEntitiesRegistry<E extends EntityDefinition<D>, D extends FieldDefinition> implements EntitiesRegistry<E, D> {

	private final Map<Class<?>, E> entitiesDefinitions = new HashMap<Class<?>, E>();

	@SuppressWarnings("unchecked")
	private Map<String, Class<?>> entities = new CaseInsensitiveMap();
	private Map<Class<?>, String> reversedEntities = new HashMap<Class<?>, String>();

	// map of entities by type
	private Map<Class<? extends EntityType>, List<Class<?>>> entitiesByTypes = new HashMap<Class<? extends EntityType>, List<Class<?>>>();

	private void add(String entityName, Class<?> entity) {
		entities.put(entityName, entity);
		reversedEntities.put(entity, entityName);
	}

	protected void addToTypes(Class<? extends EntityType> entityType, Class<?> entity) {
		List<Class<?>> relevantEntities = entitiesByTypes.get(entityType);
		if (relevantEntities == null) {
			relevantEntities = new ArrayList<Class<?>>();
			entitiesByTypes.put(entityType, relevantEntities);
		}
		relevantEntities.add(entity);

	}

	public List<Class<?>> getAll() {
		return (List<Class<?>>)entities.values();
	}

	public Class<?> getEntityClass(String entityName) {
		return entities.get(entityName);
	}

	public String getEntityName(Class<?> entity) {
		return reversedEntities.get(entity);
	}

	public void add(E entityDefinition) {
		add(entityDefinition.getEntityName(), entityDefinition.getEntityClass());
		addToTypes(entityDefinition.getType(), entityDefinition.getEntityClass());
		getEntitiesDefinitions().put(entityDefinition.getEntityClass(), entityDefinition);
	}

	public E get(Class<?> entityClass) {
		entityClass = ProxyUtil.getOriginalClass(entityClass);
		return getEntitiesDefinitions().get(entityClass);
	}

	public Map<Class<?>, E> getEntitiesDefinitions() {
		return entitiesDefinitions;
	}

	public List<Class<?>> getByType(Class<? extends EntityType> entityType) {
		return entitiesByTypes.get(entityType);
	}

	public E getSingleEntityDefinition(Class<? extends EntityType> entityType) throws RegistryException {
		List<Class<?>> matchingTypes = getByType(entityType);

		if (matchingTypes.size() == 0) {
			throw (new RegistryException(MessageFormat.format("Entity {0} not defined in the registry", entityType)));
		}

		if (matchingTypes.size() > 1) {
			throw (new RegistryException(
					MessageFormat.format("Found {0} matching entities in the registry", matchingTypes.size())));
		}

		return get(matchingTypes.get(0));
	}

	public void clear() {
		entities.clear();
		reversedEntities.clear();
		entitiesByTypes.clear();
	}
}
