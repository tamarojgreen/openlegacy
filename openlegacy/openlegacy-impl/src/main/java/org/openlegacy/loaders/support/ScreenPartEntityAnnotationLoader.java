package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
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

	@SuppressWarnings("unused")
	private final static Log logger = LogFactory.getLog(ScreenPartEntityAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenPart.class;
	}

	public void load(HostEntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		@SuppressWarnings("unused")
		ScreenPart screenPartEntity = (ScreenPart)annotation;

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenPartEntityDefinition screenPartEntityDefinition = new SimpleScreenPartEntityDefinition(containingClass);
		screenEntitiesRegistry.addPart(screenPartEntityDefinition);
	}
}
