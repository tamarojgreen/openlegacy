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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimplePasswordFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleScreenListFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.loaders.support.AbstractFieldAnnotationLoader;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Loads the field's {@link org.openlegacy.annotations.screen.ScreenField} annotation information to the
 * {@link org.openlegacy.terminal.definitions.ScreenFieldDefinition}
 * 
 * @author Roi Mor
 * 
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScreenFieldAnnotationLoader extends AbstractFieldAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenFieldAnnotationLoader.class);

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenField.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Field field, Annotation annotation, Class<?> containingClass,
			int fieldOrder) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenField fieldAnnotation = (ScreenField)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);

		SimpleTerminalPosition position = SimpleTerminalPosition.newInstance(fieldAnnotation.row(), fieldAnnotation.column());

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
			int width = fieldAnnotation.endColumn() - fieldAnnotation.column() + 1;
			int endRow = fieldAnnotation.endRow() > 0 ? fieldAnnotation.endRow() : fieldAnnotation.row();
			int height = endRow - fieldAnnotation.row() + 1;
			screenFieldDefinition.setLength(width * height);
			// TODO calculate length for "snake" field
		}

		if (fieldAnnotation.endRow() > fieldAnnotation.row()) {
			if (fieldAnnotation.endRow() < fieldAnnotation.row()) {
				throw (new RegistryException(MessageFormat.format(
						"End row is smaller then row for field {0}. (can be removed for same row)", field.getName())));
			}
			if (fieldAnnotation.endColumn() == 0) {
				throw (new RegistryException(MessageFormat.format(
						"End column must be defined for multiple rows in field {0} in entity {1}. (can be removed for same row)",
						field.getName(), containingClass.getSimpleName())));
			}

			screenFieldDefinition.setEndPosition(
					SimpleTerminalPosition.newInstance(fieldAnnotation.endRow(), fieldAnnotation.endColumn()));

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
		if (!fieldAnnotation.defaultValue().equals(AnnotationConstants.NULL)) {
			screenFieldDefinition.setDefaultValue(fieldAnnotation.defaultValue());
		}
		screenFieldDefinition.setJavaType(field.getType());

		screenFieldDefinition.setHelpText(fieldAnnotation.helpText());
		screenFieldDefinition.setKey(fieldAnnotation.key());
		screenFieldDefinition.setKeyIndex(fieldAnnotation.keyIndex());
		screenFieldDefinition.setInternal(fieldAnnotation.internal());
		screenFieldDefinition.setGlobal(fieldAnnotation.global());
		screenFieldDefinition.setTableKey(fieldAnnotation.tableKey());
		screenFieldDefinition.setForceUpdate(fieldAnnotation.forceUpdate());

		if (!fieldAnnotation.nullValue().equals(AnnotationConstants.NULL)) {
			screenFieldDefinition.setNullValue(fieldAnnotation.nullValue());
		}
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
		screenFieldDefinition.setEnableLookup(fieldAnnotation.enableLookup());
		if (screenFieldDefinition.isEnableLookup() && fieldAnnotation.lookupAction() != TerminalActions.NONE.class) {
			screenFieldDefinition.setLookupAction(ReflectionUtil.newInstance(fieldAnnotation.lookupAction()));
		}

		screenFieldDefinition.setRegularExpression(fieldAnnotation.regularExpression());
		screenFieldDefinition.setRequired(fieldAnnotation.required());
		screenFieldDefinition.setInvalidMessage(fieldAnnotation.invalidMessage());

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

		screenFieldDefinition.setExpression(fieldAnnotation.expression());
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
			screenFieldDefinition.setFieldTypeDefinition(new SimpleScreenListFieldTypeDefinition());
		} else if (field.getType().isEnum()) {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleEnumFieldTypeDefinition());
		} else {
			screenFieldDefinition.setFieldTypeDefinition(new SimpleTextFieldTypeDefinition());
		}
	}

}
