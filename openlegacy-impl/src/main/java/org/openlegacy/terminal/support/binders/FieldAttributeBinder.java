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
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;

import java.util.Collection;

import javax.inject.Inject;

public class FieldAttributeBinder implements ScreenEntityBinder {

	private final static Log logger = LogFactory.getLog(FieldAttributeBinder.class);

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot snapshot) {

		Class<? extends Object> class1 = ProxyUtil.getOriginalClass(screenEntity.getClass());

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(snapshot, class1);

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getAttribute() == FieldAttributeType.Value) {
				continue;
			}

			TerminalPosition position = ScreenBinderLogic.retrievePosition(fieldDefinition, snapshot);
			if (position == null) {
				logger.warn("A field was not found for field:" + fieldDefinition.getName());
				continue;
			}
			TerminalField terminalField = snapshot.getField(position);
			if (terminalField == null) {
				continue;
			}

			String fieldName = fieldDefinition.getName();
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			if (fieldDefinition.getAttribute() == FieldAttributeType.Editable) {

				fieldAccessor.setFieldValue(fieldName, terminalField.isEditable());

			} else if (fieldDefinition.getAttribute() == FieldAttributeType.Color) {
				fieldAccessor.setFieldValue(fieldName, terminalField.getColor());
			} else if (fieldDefinition.getAttribute() == FieldAttributeType.BackColor) {
				fieldAccessor.setFieldValue(fieldName, terminalField.getBackColor());
			}
		}
	}

	@Override
	public void populateAction(TerminalSendAction sendAction, TerminalSnapshot snapshot, Object entity) {

	}
}
