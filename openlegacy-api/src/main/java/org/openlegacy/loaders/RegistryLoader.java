package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;

import java.util.Collection;

public interface RegistryLoader {

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Collection<ClassAnnotationsLoader> annotationLoaders,
			Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Collection<FieldLoader> fieldLoaders);
}
