package org.openlegacy.loaders;

import org.openlegacy.HostEntitiesRegistry;

import java.lang.annotation.Annotation;

public interface FieldAnnotationsLoader {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(HostEntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation, Class<?> containingClass);
}
