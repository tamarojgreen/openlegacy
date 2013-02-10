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
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleListFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimplePasswordFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.binders.ScreenBinderLogic;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenBinderLogic.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenField.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenField fieldAnnotation = (ScreenField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);

		SimpleTerminalPosition position = SimpleTerminalPosition.newInstance(fieldAnnotation.row(), fieldAnnotation.column());
		if (screenEntityDefinition != null && !screenEntityDefinition.getScreenSize().contains(position)) {
			throw (new RegistryException(MessageFormat.format("Field {0} is out of screen {1} bounds", field.getName(),
					screenEntityDefinition.getEntityClassName())));
		}

		String fieldName = field.getName();
		SimpleScreenFieldDefinition screenFieldDefinition = new SimpleScreenFieldDefinition(fieldName,
				fieldAnnotation.fieldType());
		screenFieldDefinition.setPosition(position);

		if (fieldAnnotation.endColumn() == 0) {
			// might be null for screen part
			if (screenEntityDefinition == null || screenEntityDefinition.getSnapshot() == null) {
				screenFieldDefinition.setLength(0);
			} else {
				if (screenEntityDefinition != null && screenEntityDefinition.getSnapshot() != null) {
					TerminalField terminalField = screenEntityDefinition.getSnapshot().getField(position);
					if (terminalField != null) {
						int length = terminalField.getLength();
						screenFieldDefinition.setLength(length);
						logger.debug(MessageFormat.format(
								"Applying terminal field length {0} to field {1}.{2} which has no end column defined", length,
								screenEntityDefinition.getEntityName(), field.getName()));
					}
				}
			}
		} else {
			screenFieldDefinition.setLength(fieldAnnotation.endColumn() - fieldAnnotation.column() + 1);
		}

		if (fieldAnnotation.endRow() > 0) {
			if (fieldAnnotation.endRow() <= fieldAnnotation.row()) {
				throw (new RegistryException(MessageFormat.format(
						"End row must be greater then row for field {0}. (can be removed for same row)", field.getName())));
			}
			if (fieldAnnotation.endColumn() == 0) {
				throw (new RegistryException(MessageFormat.format(
						"End column must be defined for multiple rows in field {0}. (can be removed for same row)",
						field.getName())));
			}

			screenFieldDefinition.setEndPosition(SimpleTerminalPosition.newInstance(fieldAnnotation.endRow(),
					fieldAnnotation.endColumn()));

			screenFieldDefinition.setRectangle(fieldAnnotation.rectangle());
		}

		if (fieldAnnotation.labelColumn() > 0) {
			SimpleTerminalPosition labelPosition = SimpleTerminalPosition.newInstance(fieldAnnotation.row(),
					fieldAnnotation.labelColumn());
			screenFieldDefinition.setLabelPosition(labelPosition);
		}

		screenFieldDefinition.setEditable(fieldAnnotation.editable());
		screenFieldDefinition.setPassword(fieldAnnotation.password());
		screenFieldDefinition.setRightToLeft(fieldAnnotation.rightToLeft());

		if (fieldAnnotation.displayName().equals(AnnotationConstants.NULL)) {
			screenFieldDefinition.setDisplayName(StringUtil.toDisplayName(fieldName));
		} else {
			screenFieldDefinition.setDisplayName(fieldAnnotation.displayName());
		}

		screenFieldDefinition.setSampleValue(fieldAnnotation.sampleValue());
		screenFieldDefinition.setJavaType(field.getType());

		screenFieldDefinition.setHelpText(fieldAnnotation.helpText());
		screenFieldDefinition.setKey(fieldAnnotation.key());
		if (logger.isDebugEnabled()) {

			logger.debug(MessageFormat.format("The annotation of the attribute attribute is {0} ", fieldAnnotation.attribute()));
		}
		screenFieldDefinition.setAttribute(fieldAnnotation.attribute());
		if (!fieldAnnotation.when().equals("")) {
			screenFieldDefinition.setWhenFilter(fieldAnnotation.when());
		}
		if (!fieldAnnotation.unless().equals("")) {
			screenFieldDefinition.setUnlessFilter(fieldAnnotation.unless());
		}
		setupFieldType(field, screenFieldDefinition);

		// look in screen entities
		if (screenEntityDefinition != null) {
			screenEntityDefinition.getFieldsDefinitions().put(fieldName, screenFieldDefinition);
		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				screenFieldDefinition.setName(fieldName);
				screenPart.getFieldsDefinitions().put(fieldName, screenFieldDefinition);
			}

		}

	}

	private static void setupFieldType(Field field, SimpleScreenFieldDefinition screenFieldDefinition) {
		// set number type definition - may be overridden by ScreenNumericFieldAnnotationLoader to fill in specific numeric
		// properties

		if (Number.class.isAssignableFrom(field.getType())) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleNumericFieldTypeDefinition());
		}
		// set date type definition - may be overridden by ScreenDateFieldAnnotationLoader to fill in specific date properties
		else if (Date.class.isAssignableFrom(field.getType())) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleDateFieldTypeDefinition());
		} else if (screenFieldDefinition.isPassword()) {
			screenFieldDefinition.setFieldTypeDefinition(new SimplePasswordFieldTypeDefinition());
		} else if (java.util.List.class == field.getType()) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleListFieldTypeDefinition());
		} else {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleTextFieldTypeDefinition());
		}
	}

}
