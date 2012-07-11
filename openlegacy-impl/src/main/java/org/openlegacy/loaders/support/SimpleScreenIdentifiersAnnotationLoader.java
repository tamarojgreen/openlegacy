/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class SimpleScreenIdentifiersAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(SimpleScreenIdentifiersAnnotationLoader.class);
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenIdentifiers.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry screenEntitiesRegistry, Annotation annotation, Class<?> containingClass) {
		this.screenEntitiesRegistry = (ScreenEntitiesRegistry)screenEntitiesRegistry;
		processScreenEntity((ScreenIdentifiers)annotation, containingClass);
	}

	public void processScreenEntity(ScreenIdentifiers screenIdentifiers, Class<?> screenEntityClass) {

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntityClass);
		Identifier[] identifiers = screenIdentifiers.identifiers();
		if (identifiers.length > 0) {
			ScreenIdentification screenIdentification = screenEntityDefinition.getScreenIdentification();
			for (Identifier identifier : identifiers) {
				TerminalPosition position = SimpleTerminalPosition.newInstance(identifier.row(), identifier.column());
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
