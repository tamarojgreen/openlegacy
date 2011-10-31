package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenPosition;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

public class ScreenEntityAnnotationLoader implements ClassAnnotationsLoader {

	private final static Log logger = LogFactory.getLog(ScreenEntityAnnotationLoader.class);
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenEntity.class;
	}

	public void load(HostEntitiesRegistry<?, ?> screenEntitiesRegistry, Annotation annotation, Class<?> containingClass) {
		this.screenEntitiesRegistry = (ScreenEntitiesRegistry)screenEntitiesRegistry;
		processScreenEntity((ScreenEntity)annotation, containingClass);
	}

	public void processScreenEntity(ScreenEntity screenEntity, Class<?> screenEntityClass) {
		String screenName = screenEntity.name().length() > 0 ? screenEntity.name() : screenEntityClass.getSimpleName();
		SimpleScreenEntityDefinition screenEntityDefinition = new SimpleScreenEntityDefinition(screenName, screenEntityClass);
		screenEntityDefinition.setName(screenName);
		screenEntityDefinition.setType(screenEntity.screenType());
		logger.info(MessageFormat.format("Screen \"{0}\" was added to the screen registry ({1})", screenName,
				screenEntityClass.getName()));
		addIdentifiers(screenEntityDefinition, screenEntity);

		screenEntitiesRegistry.add(screenEntityDefinition);
	}

	private static void addIdentifiers(SimpleScreenEntityDefinition screenEntityDefinition, ScreenEntity screenEntity) {
		if (screenEntity.identifiers().length > 0) {
			Identifier[] identifiers = screenEntity.identifiers();
			SimpleScreenIdentification screenIdentification = new SimpleScreenIdentification();
			for (Identifier identifier : identifiers) {
				ScreenPosition position = SimpleScreenPosition.newInstance(identifier.row(), identifier.column());
				String text = identifier.value();
				SimpleScreenIdentifier simpleIdentifier = new SimpleScreenIdentifier(position, text);
				screenIdentification.addIdentifier(simpleIdentifier);

				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Identifier {0} - \"{1}\" was added to the registry for screen {2}",
							position, text, screenEntityDefinition.getEntityClass()));
				}

			}
			screenEntityDefinition.setScreenIdentification(screenIdentification);
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		}
	}

}
