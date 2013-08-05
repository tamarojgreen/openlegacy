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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenNumericField;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

@Component
public class ScreenNumericFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenNumericField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenNumericField fieldAnnotation = (ScreenNumericField)annotation;

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

	private static void fillTypeDefinition(ScreenNumericField fieldAnnotation, SimpleScreenFieldDefinition fieldDefinition,
			String fieldName) {
		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @ScreenDateField is defined along @ScreenField annotation",
				fieldName));

		if (!Number.class.isAssignableFrom(fieldDefinition.getJavaType())) {
			throw (new RegistryException("A field marked with @ScreenNumericField must be of numeric"));
		}
		fieldDefinition.setFieldTypeDefinition(new SimpleNumericFieldTypeDefinition(fieldAnnotation.minimumValue(),
				fieldAnnotation.maximumValue()));
	}

}
