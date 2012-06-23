package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;

import java.lang.reflect.Field;

public interface FieldLoader {

	@SuppressWarnings("rawtypes")
	boolean match(EntitiesRegistry entitiesRegistry, Field field);

	@SuppressWarnings("rawtypes")
	void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass);

}
