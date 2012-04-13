package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldAnnotationsLoader extends Comparable<FieldAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, Field field, Annotation annotation,
			Class<?> containingClass);
}
