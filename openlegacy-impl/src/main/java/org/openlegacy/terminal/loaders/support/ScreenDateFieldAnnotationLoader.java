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

import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractDateFieldAnnotationLoader;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class ScreenDateFieldAnnotationLoader extends AbstractDateFieldAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenDateField.class;
	}

	@Override
	protected void fillTypeDefinition(Annotation annotation, FieldDefinition fieldDefinition, String fieldName) {

		ScreenDateField fieldAnnotation = (ScreenDateField)annotation;
		SimpleScreenFieldDefinition screenFieldDefinition = (SimpleScreenFieldDefinition)fieldDefinition;
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
			screenFieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(dayColumn > 0 ? dayColumn : null,
					monthColumn > 0 ? monthColumn : null, yearColumn > 0 ? yearColumn : null));
		} else {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition(pattern, fieldAnnotation.locale()));
		}
		screenFieldDefinition.setJavaType(Date.class);
	}

}
