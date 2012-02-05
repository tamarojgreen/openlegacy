package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.terminal.definitions.SimpleScreenPartEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenPartEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenPart.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenPart screenPartAnnotation = (ScreenPart)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		SimpleScreenPartEntityDefinition screenPartEntityDefinition = new SimpleScreenPartEntityDefinition(containingClass);
		String name = screenPartAnnotation.name().length() > 0 ? screenPartAnnotation.name()
				: StringUtil.toJavaFieldName(containingClass.getSimpleName());
		screenPartEntityDefinition.setPartName(name);
		screenEntitiesRegistry.addPart(screenPartEntityDefinition);
	}
}
