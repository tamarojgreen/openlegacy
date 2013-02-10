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
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.definitions.support.SimpleFieldWthValuesTypeDefinition;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;

import javax.inject.Inject;

@Component
public class ScreenFieldValuesAnnotationLoader extends AbstractFieldAnnotationLoader implements ApplicationContextAware {

	@Inject
	private ApplicationContext applicationContext;

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenFieldValues.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenFieldValues fieldValuesAnnotation = (ScreenFieldValues)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		String fieldName = field.getName();

		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			setfieldValuesDefinitions(fieldValuesAnnotation, fieldDefinition, fieldName);

		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenPart.getFieldsDefinitions().get(
						fieldName);
				setfieldValuesDefinitions(fieldValuesAnnotation, fieldDefinition, fieldName);
			}

		}

	}

	private void setfieldValuesDefinitions(ScreenFieldValues fieldValuesAnnotation, SimpleScreenFieldDefinition fieldDefinition,
			String fieldName) {
		ScreenRecordsProvider screenRecordsProvider = SpringUtil.getDefaultBean(applicationContext,
				fieldValuesAnnotation.provider());
		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @ScreenFieldValues is defined along @ScreenField annotation",
				fieldName));

		SimpleFieldWthValuesTypeDefinition fieldTypeDefinition = new SimpleFieldWthValuesTypeDefinition();
		fieldTypeDefinition.setRecordsProvider(screenRecordsProvider);
		fieldTypeDefinition.setSourceEntityClass(fieldValuesAnnotation.sourceScreenEntity());
		fieldTypeDefinition.setCollectAllRecords(fieldValuesAnnotation.collectAll());
		fieldTypeDefinition.setAsWindow(fieldValuesAnnotation.asWindow());
		if (fieldValuesAnnotation.displayFieldName().length() > 0) {
			fieldTypeDefinition.setDisplayFieldName(fieldValuesAnnotation.displayFieldName());
		}

		fieldDefinition.setFieldTypeDefinition(fieldTypeDefinition);

	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
