package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenEntityAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenEntity.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntity screenEntity = (ScreenEntity)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		String screenName = screenEntity.name().length() > 0 ? screenEntity.name() : containingClass.getSimpleName();
		String displayName = screenEntity.displayName().length() > 0 ? screenEntity.displayName()
				: StringUtil.toDisplayName(screenName);

		SimpleScreenEntityDefinition screenEntityDefinition = new SimpleScreenEntityDefinition(screenName, containingClass);
		screenEntityDefinition.setEntityName(screenName);
		screenEntityDefinition.setDisplayName(displayName);
		screenEntityDefinition.setType(screenEntity.screenType());
		screenEntityDefinition.setWindow(screenEntity.window());

		screenEntityDefinition.setScreenSize(new SimpleScreenSize(screenEntity.rows(), screenEntity.columns()));

		logger.info(MessageFormat.format("Screen \"{0}\" was added to the screen registry ({1})", screenName,
				containingClass.getName()));

		screenEntitiesRegistry.add(screenEntityDefinition);
	}
}
