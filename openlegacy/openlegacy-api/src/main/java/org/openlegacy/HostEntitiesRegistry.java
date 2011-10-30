package org.openlegacy;

import org.openlegacy.exceptions.RegistryException;

import java.util.List;

/**
 * A common interface for defining a registry for host entity, and retrieving an entity class by name
 * 
 */
@SuppressWarnings("rawtypes")
public interface HostEntitiesRegistry<D extends HostEntityDefinition> {

	Class<?> getEntityClass(String entityName);

	String getEntityName(Class<?> hostEntity);

	List<Class<?>> getByType(Class<? extends HostEntityType> hostEntityType);

	D getFirstEntityDefinition(Class<? extends HostEntityType> hostEntityType) throws RegistryException;

	List<Class<?>> getAll();

	void add(D entityDefinition);

	D get(Class<?> entityClass);
}
