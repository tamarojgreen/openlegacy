package org.openlegacy.cache.loaders;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.cache.Cacheable;
import org.openlegacy.cache.services.CacheableEntitiesRegistry;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class CacheableAnnotationLoader extends AbstractClassAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == Cacheable.class;
	}

	@Override
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		Cacheable cacheable = (Cacheable) annotation;
		int expiry = (cacheable.expiry() >= 0) ? cacheable.expiry() : CacheableEntitiesRegistry.DEFAULT_EXPIRY;
		getCacheableEntitiesRegistry().addEntity(containingClass, expiry, cacheable.getActions(),
				new Class<?>[] {} /*cacheable.putActions()*/, cacheable.removeActions());
	}

	public CacheableEntitiesRegistry getCacheableEntitiesRegistry() {
		return getBeanFactory().getBean(CacheableEntitiesRegistry.class);
	}

}
