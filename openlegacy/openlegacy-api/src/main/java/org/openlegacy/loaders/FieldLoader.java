package org.openlegacy.loaders;

import org.openlegacy.HostEntitiesRegistry;

import java.lang.reflect.Field;

public interface FieldLoader {

	@SuppressWarnings("rawtypes")
	boolean match(HostEntitiesRegistry entitiesRegistry, Field field);

	@SuppressWarnings("rawtypes")
	void load(HostEntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass);

}
