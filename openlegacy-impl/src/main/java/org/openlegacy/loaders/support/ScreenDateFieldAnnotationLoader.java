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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class ScreenDateFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenDateField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenDateField fieldAnnotation = (ScreenDateField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();
		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenPart.getFieldsDefinitions().get(
						fieldName);
				fillTypeDefinition(fieldAnnotation, fieldDefinition, fieldName);
			}

		}

	}

	private static void fillTypeDefinition(ScreenDateField fieldAnnotation, SimpleScreenFieldDefinition fieldDefinition,
			String fieldName) {
		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @ScreenDateField is defined along @ScreenField annotation",
				fieldName));

		if (fieldDefinition.getJavaType() != Date.class) {
			throw (new RegistryException(MessageFormat.format(
					"Field {0} marked with @ScreenDateField must be of type java.util.Date", fieldName)));
		}

		int dayColumn = fieldAnnotation.dayColumn();
		int monthColumn = fieldAnnotation.monthColumn();
		int yearColumn = fieldAnnotation.yearColumn();
		String pattern = fieldAnnotation.pattern();
		// set to null if default value is 0. null should be used to determine which date fields are enabled/disabled (to pick
		// just month+year for example)
		if (pattern.equals("")) {
			fieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(dayColumn > 0 ? dayColumn : null,
					monthColumn > 0 ? monthColumn : null, yearColumn > 0 ? yearColumn : null));
		} else {
			fieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(pattern, fieldAnnotation.locale()));
		}
		fieldDefinition.setJavaType(Date.class);
	}

}
