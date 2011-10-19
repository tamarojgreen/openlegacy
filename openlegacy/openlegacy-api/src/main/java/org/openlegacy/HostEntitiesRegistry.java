package org.openlegacy;

import java.util.List;

/**
 * A common interface for defining a registry for host entity, and retrieving an entity class by name
 * 
 */
public interface HostEntitiesRegistry {

	void add(String entityName, Class<?> hostEntity);

	Class<?> get(String entityName);

	String get(Class<?> hostEntity);

	List<Class<?>> getAll();

}
