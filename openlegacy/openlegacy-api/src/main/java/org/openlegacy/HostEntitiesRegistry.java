package org.openlegacy;

import org.openlegacy.exceptions.RegistryException;

import java.util.List;

/**
 * A common interface for defining a registry for host entity, and retrieving an entity class by name
 * 
 */
public interface HostEntitiesRegistry<H extends HostEntityDefinition<D>, D extends FieldDefinition> {

	Class<?> getEntityClass(String entityName);

	String getEntityName(Class<?> hostEntity);

	List<Class<?>> getByType(Class<? extends HostEntityType> hostEntityType);

	H getFirstEntityDefinition(Class<? extends HostEntityType> hostEntityType) throws RegistryException;

	List<Class<?>> getAll();

	void add(H entityDefinition);

	H get(Class<?> entityClass);

	void clear();
}
