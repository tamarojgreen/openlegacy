package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenPosition;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

public class SimpleScreenIdentifiersAnnotationLoader implements ClassAnnotationsLoader {

	private final static Log logger = LogFactory.getLog(SimpleScreenIdentifiersAnnotationLoader.class);
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenIdentifiers.class;
	}

	public void load(HostEntitiesRegistry<?, ?> screenEntitiesRegistry, Annotation annotation, Class<?> containingClass) {
		this.screenEntitiesRegistry = (ScreenEntitiesRegistry)screenEntitiesRegistry;
		processScreenEntity((ScreenIdentifiers)annotation, containingClass);
	}

	public void processScreenEntity(ScreenIdentifiers screenIdentifiers, Class<?> screenEntityClass) {

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntityClass);
		Identifier[] identifiers = screenIdentifiers.identifiers();
		if (identifiers.length > 0) {
			ScreenIdentification screenIdentification = screenEntityDefinition.getScreenIdentification();
			for (Identifier identifier : identifiers) {
				ScreenPosition position = SimpleScreenPosition.newInstance(identifier.row(), identifier.column());
				String text = identifier.value();
				SimpleScreenIdentifier simpleIdentifier = new SimpleScreenIdentifier(position, text);
				screenIdentification.addIdentifier(simpleIdentifier);

				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Identifier {0} - \"{1}\" was added to the registry for screen {2}",
							position, text, screenEntityClass));
				}

			}
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		}
	}
}
