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
import java.lang.reflect.Field;
import java.text.MessageFormat;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenDynamicField;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.SimpleDynamicFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class ScreenDynamicFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenDynamicField.class;
	}

	protected SimpleDynamicFieldTypeDefinition fillTypeDefinition(Annotation annotation, FieldDefinition fieldDefinition, String fieldName) {
		ScreenDynamicField fieldAnnotation = (ScreenDynamicField)annotation;

		String text = fieldAnnotation.text();
		if (StringUtil.isEmpty(text)){
			throw (new RegistryException("Dynamic field leading text is missing for field: " + fieldDefinition.getName()));
		}
		
		int row = fieldAnnotation.row();
		int column = fieldAnnotation.column();
		int endColumn = fieldAnnotation.endColumn();
		int endRow = fieldAnnotation.endRow();
		int fieldsOffset = fieldAnnotation.fieldOffset();

		return new SimpleDynamicFieldTypeDefinition(text, row, column, endColumn, endRow, fieldsOffset);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry) entitiesRegistry;

		ScreenDynamicField fieldAnnotation = (ScreenDynamicField) annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry
				.get(containingClass);
		String fieldName = field.getName();
		SimpleDynamicFieldTypeDefinition dynamicFieldDefinition = null;
		SimpleScreenFieldDefinition fieldDefinition = null;
		// look in screen entities
		if (screenEntityDefinition != null) {
			fieldDefinition = (SimpleScreenFieldDefinition) screenEntityDefinition
					.getFieldsDefinitions().get(fieldName);
			dynamicFieldDefinition = fillTypeDefinition(fieldAnnotation,
					fieldDefinition, fieldName);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry
					.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}",
						screenPart.getPartName(), fieldName);
				fieldDefinition = (SimpleScreenFieldDefinition) screenPart
						.getFieldsDefinitions().get(fieldName);
				dynamicFieldDefinition = fillTypeDefinition(fieldAnnotation,
						fieldDefinition, fieldName);
			}
		}

		fieldDefinition.setDynamicFieldDefinition(dynamicFieldDefinition);
	}


}
