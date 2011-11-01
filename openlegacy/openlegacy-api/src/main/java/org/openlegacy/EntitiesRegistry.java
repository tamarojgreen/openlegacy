package org.openlegacy;

import org.openlegacy.exceptions.RegistryException;

import java.util.List;

/**
 * A common interface for defining a registry for host entity, and retrieving an entity class by name
 * 
 */
public interface EntitiesRegistry {

	Class<?> getEntityClass(String entityName);

	String getEntityName(Class<?> hostEntity);

	List<Class<?>> getByType(Class<? extends EntityType> hostEntityType);

	FieldDefinition getFirstEntityDefinition(Class<? extends EntityType> hostEntityType) throws RegistryException;

	List<Class<?>> getAll();

	void add(EntityDefinition entityDefinition);

	EntityDefinition get(Class<?> entityClass);
}
