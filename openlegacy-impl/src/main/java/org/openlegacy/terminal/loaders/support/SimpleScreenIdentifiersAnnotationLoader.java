/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.loaders.support;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.FieldColorIdentifier;
import org.openlegacy.terminal.support.FieldEditableIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
		Assert.notNull(screenEntityDefinition, MessageFormat.format(
				"@ScreenIdentifiers for class {0} should be define along @ScreenEntity annotation", screenEntityClass));

		Identifier[] identifiers = screenIdentifiers.identifiers();
		if (identifiers.length > 0) {
			ScreenIdentification screenIdentification = screenEntityDefinition.getScreenIdentification();
			for (Identifier identifier : identifiers) {
				TerminalPosition position = SimpleTerminalPosition.newInstance(identifier.row(), identifier.column());
				ScreenIdentifier simpleIdentifier = null;  
				if (identifier.attribute() == FieldAttributeType.Editable){
					if (!identifier.value().equals("true") && !identifier.value().equals("false")){
						throw(new RegistryException("Identifier of type Editable must have \"true\" or \"false\" as value"));
					}
					Boolean editable = Boolean.valueOf(identifier.value());
					simpleIdentifier = new FieldEditableIdentifier(position, editable);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Identifier {0} - editable: {1} , was added to the registry for screen {2}",
								position, editable, screenEntityClass));
					}
					
				}
				else if (identifier.attribute() == FieldAttributeType.Color){
					if (Color.valueOf(identifier.value()) == null){
						throw(new RegistryException("Identifier of type Color must have org.openlegacy.terminal.Color as value"));
					}
					Color color = Color.valueOf(identifier.value());
					simpleIdentifier = new FieldColorIdentifier(position, color);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Identifier {0} - color: {1} , was added to the registry for screen {2}",
								position, color, screenEntityClass));
					}
					
				}
				else{
					String text = identifier.value();
					simpleIdentifier = new SimpleScreenIdentifier(position, text,
							screenEntityDefinition.isRightToLeft());
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Identifier {0} - \"{1}\" was added to the registry for screen {2}",
								position, text, screenEntityClass));
					}
				}
				screenIdentification.addIdentifier(simpleIdentifier);


			}
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getEntityClass()));
		} else {
			logger.warn(MessageFormat.format(
					"*** A screen without identifications was found \"{0}\" Please verify its a valid screen",
					screenEntityDefinition.getEntityClass()));

		}
	}
}
