package org.openlegacy.loaders;

import org.openlegacy.HostEntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

import java.lang.annotation.Annotation;

public interface FieldAnnotationsLoader {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(BeanFactory beanFactory, HostEntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation,
			Class<?> containingClass);
}
