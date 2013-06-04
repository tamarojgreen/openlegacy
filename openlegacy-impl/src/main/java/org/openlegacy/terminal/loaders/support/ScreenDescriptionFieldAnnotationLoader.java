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
package org.openlegacy.terminal.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenDescriptionField;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class ScreenDescriptionFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenDescriptionFieldAnnotationLoader.class);

	private String descriptionSuffix = "Description";

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenDescriptionField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenDescriptionField fieldAnnotation = (ScreenDescriptionField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fillDescriptionDefinition(fieldAnnotation, screenEntityDefinition, fieldDefinition);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenPart.getFieldsDefinitions().get(
						fieldName);
				fillDescriptionDefinition(fieldAnnotation, screenEntityDefinition, fieldDefinition);
			}

		}

	}

	private void fillDescriptionDefinition(ScreenDescriptionField fieldAnnotation, ScreenEntityDefinition screenEntityDefinition,
			SimpleScreenFieldDefinition screenFieldDefinition) {
		SimpleScreenFieldDefinition descriptionFieldDefinition = new SimpleScreenFieldDefinition();
		String fieldName = screenFieldDefinition.getName() + descriptionSuffix;
		descriptionFieldDefinition.setName(fieldName);
		int row = fieldAnnotation.row() > 0 ? fieldAnnotation.row() : screenFieldDefinition.getPosition().getRow();
		SimpleTerminalPosition position = SimpleTerminalPosition.newInstance(row, fieldAnnotation.column());
		descriptionFieldDefinition.setPosition(position);

		int length = 0;
		if (fieldAnnotation.endColumn() == 0) {
			// might be null for screen part
			if (screenEntityDefinition == null || screenEntityDefinition.getSnapshot() == null) {
				descriptionFieldDefinition.setLength(0);
			} else {
				if (screenEntityDefinition != null && screenEntityDefinition.getSnapshot() != null) {
					TerminalField terminalField = screenEntityDefinition.getSnapshot().getField(position);
					if (terminalField != null) {
						length = terminalField.getLength();
						descriptionFieldDefinition.setLength(length);
						logger.debug(MessageFormat.format(
								"Applying terminal field length {0} to field {1}.{2}{3} which has no end column defined", length,
								screenEntityDefinition.getEntityName(), fieldName, descriptionSuffix));
					}
				}
			}
		} else {
			length = fieldAnnotation.endColumn() - fieldAnnotation.column() + 1;
			descriptionFieldDefinition.setLength(length);
		}
		descriptionFieldDefinition.setEndPosition(position.moveBy(length));
		screenFieldDefinition.setDescriptionFieldDefinition(descriptionFieldDefinition);
	}

	public void setDescriptionSuffix(String descriptionSuffix) {
		this.descriptionSuffix = descriptionSuffix;
	}
}
