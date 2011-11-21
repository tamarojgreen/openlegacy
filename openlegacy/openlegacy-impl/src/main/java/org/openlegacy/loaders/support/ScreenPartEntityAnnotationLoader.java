package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenPartEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenPartEntityAnnotationLoader implements ClassAnnotationsLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenPart.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenPartEntityDefinition screenPartEntityDefinition = new SimpleScreenPartEntityDefinition(containingClass);
		screenEntitiesRegistry.addPart(screenPartEntityDefinition);
	}
}
