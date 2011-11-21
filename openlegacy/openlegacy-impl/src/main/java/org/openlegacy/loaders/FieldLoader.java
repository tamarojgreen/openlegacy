package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Field;

public interface FieldLoader {

	@SuppressWarnings("rawtypes")
	boolean match(EntitiesRegistry entitiesRegistry, Field field);

	@SuppressWarnings("rawtypes")
	void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass);

}
