package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;

import java.lang.annotation.Annotation;

public interface ClassAnnotationsLoader extends Comparable<ClassAnnotationsLoader> {

	boolean match(Annotation annotation);

	void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass);
}
