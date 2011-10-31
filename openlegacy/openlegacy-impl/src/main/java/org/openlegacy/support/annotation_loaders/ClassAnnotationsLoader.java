package org.openlegacy.support.annotation_loaders;

import org.openlegacy.HostEntitiesRegistry;

import java.lang.annotation.Annotation;

public interface ClassAnnotationsLoader {

	boolean match(Annotation annotation);

	void load(HostEntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass);
}
