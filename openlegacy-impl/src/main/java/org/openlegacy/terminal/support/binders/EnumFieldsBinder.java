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
package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.DisplayItem;
import org.openlegacy.definitions.EnumFieldTypeDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Enum binder implementation for binding enum based fields to a screenEntity based on the enum getValue property
 * 
 * @author Roi Mor
 */
public class EnumFieldsBinder implements ScreenEntityBinder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	private final static Log logger = LogFactory.getLog(EnumFieldsBinder.class);

	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) throws EntityNotFoundException,
			ScreenEntityNotAccessibleException {

		ScreenPojoFieldAccessor fieldAccessor = null;

		Class<? extends Object> class1 = ProxyUtil.getOriginalClass(screenEntity.getClass());

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(terminalSnapshot,
				class1);

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (!Enum.class.isAssignableFrom(fieldDefinition.getJavaType())
					|| fieldDefinition.getAttribute() != FieldAttributeType.Value) {
				continue;
			}
			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}
			EnumFieldTypeDefinition fieldTypeDefinition = (EnumFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			TerminalPosition position = ScreenBinderLogic.retrievePosition(fieldDefinition, terminalSnapshot);
			;

			if (position == null) {
				logger.warn("A field was not found for field:" + fieldDefinition.getName());
				continue;
			}

			TerminalField enumField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(position.getRow(),
					position.getColumn()));

			if (enumField.isHidden()) {
				logger.debug("A hidden field was not bound " + fieldDefinition.getName());
				return;
			}

			if (!StringUtil.isEmpty(enumField.getValue())) {

				// find the DisplayItem by host value, and return the enum object
				DisplayItem enum1 = fieldTypeDefinition.getEnums().get(enumField.getValue().trim());
				if (enum1 != null) {
					fieldAccessor.setFieldValue(fieldDefinition.getName(), enum1.getValue());
				}
			}

		}
	}

	@Override
	public void populateAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object entity) {

		if (entity == null) {
			return;
		}

		Assert.isTrue(entity instanceof ScreenEntity, "screen entity must implement ScreenEntity interface");

		ScreenEntity screenEntity = (ScreenEntity)entity;

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(terminalSnapshot,
				screenEntity.getClass());

		if (fieldDefinitions == null) {
			return;
		}

		ScreenPojoFieldAccessor fieldAccessor = null;

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (!Enum.class.isAssignableFrom(fieldDefinition.getJavaType())) {
				continue;
			}

			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}

			if (!fieldDefinition.isEditable()) {
				continue;
			}
			EnumFieldTypeDefinition fieldTypeDefinition = (EnumFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			TerminalPosition position = ScreenBinderLogic.retrievePosition(fieldDefinition, terminalSnapshot);
			TerminalField enumField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(position.getRow(),
					position.getColumn()));

			Object fieldValue = fieldAccessor.evaluateFieldValue(fieldDefinition.getName());

			Collection<Object> enums = fieldTypeDefinition.getDisplayValues().keySet();
			for (Object enumValue : enums) {
				if (enumValue.equals(fieldValue) && fieldDefinition.isEditable()) {
					// once enum from the POJO is found, find the associated host value
					enumField.setValue((String)ReflectionUtil.invoke(enumValue, "getValue"));
					sendAction.getFields().add(enumField);
				}
			}
		}
	}

}
