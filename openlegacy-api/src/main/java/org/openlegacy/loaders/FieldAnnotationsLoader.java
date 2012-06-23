package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldAnnotationsLoader extends Comparable<FieldAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass);
}
