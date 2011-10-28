package org.openlegacy;

import java.util.List;

/**
 * A common interface for defining a registry for host entity, and retrieving an entity class by name
 * 
 */
public interface HostEntitiesRegistry<D extends HostEntityDefinition> {

	Class<?> getEntityClass(String entityName);

	String getEntityName(Class<?> hostEntity);

	List<Class<?>> getAll();

	void add(D entityDefinition);

	D get(Class<?> entityClass);
}
