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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenListField;
import org.openlegacy.definitions.support.SimpleScreenListFieldTypeDefinition;
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
import java.util.List;

@Component
public class ScreenListFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenListField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenListField fieldAnnotation = (ScreenListField)annotation;
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

	private static void fillTypeDefinition(ScreenListField fieldAnnotation, SimpleScreenFieldDefinition fieldDefinition,
			String fieldName) {

		Assert.notNull(fieldDefinition, MessageFormat.format(
				"Field definition for field {0} not found. Verify @ScreenListField is defined along @ScreenField annotation",
				fieldName));
		System.out.println(fieldDefinition.getJavaType());
		if (fieldDefinition.getJavaType() != List.class && fieldDefinition.getJavaType() != String[].class) {
			throw (new RegistryException(MessageFormat.format(
					"Field {0} marked with @ScreenListField must be of type java.util.List", fieldName)));
		}

		Assert.isTrue(fieldAnnotation.count() > 1,
				MessageFormat.format("count in @ScreenListField must be greater than 1", fieldName));
		int[] gaps = fieldAnnotation.gaps();
		Assert.isTrue(gaps.length == (fieldAnnotation.count() - 1) || gaps.length == 1);

		for (int gap : gaps) {
			Assert.isTrue(gap > 1,
					MessageFormat.format("each element in gaps  @ScreenListField must be greater than 1", fieldName));
		}
		fieldDefinition.setFieldTypeDefinition(new SimpleScreenListFieldTypeDefinition(fieldAnnotation.fieldLength(),
				fieldAnnotation.count(), fieldAnnotation.gaps()));
	}
}
