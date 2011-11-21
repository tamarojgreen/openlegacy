package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;

public interface FieldAnnotationsLoader {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation,
			Class<?> containingClass);
}
