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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
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
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DateFieldsPatternBinder implements ScreenEntityBinder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	private final static Log logger = LogFactory.getLog(DateFieldsBinder.class);

	@Override
	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) throws EntityNotFoundException,
			ScreenEntityNotAccessibleException {

		ScreenPojoFieldAccessor fieldAccessor = null;

		Class<? extends Object> class1 = ProxyUtil.getOriginalClass(screenEntity.getClass());

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(terminalSnapshot,
				class1);

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getJavaType() != Date.class) {
				continue;
			}
			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}
			DateFieldTypeDefinition fieldTypeDefinition = (DateFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			Assert.notNull(fieldTypeDefinition, "A field of type date is defined without @ScreenDateField annotation");
			String pattern = fieldTypeDefinition.getPattern();
			if (pattern == null) {
				continue;
			}

			TerminalPosition position = ScreenBinderLogic.retrievePosition(fieldDefinition, terminalSnapshot);
			if (position == null){
				logger.warn("A field was not found for field:" + fieldDefinition.getName());
				continue;
			}
			TerminalField dateField = terminalSnapshot.getField(position);
			SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));

			try {
				if (dateField.isHidden()) {
					logger.debug("A hidden field was not bound " + fieldDefinition.getName());
					return;
				}

				String value = dateField.getValue();
				if (StringUtils.isBlank(value)) {
					continue;
				}
				value = fieldFormatter.format(value);
				if (!value.equals(fieldDefinition.getNullValue())) {
					if (value.length() == 5) {
						value = "0" + value;
					}
					
					Date dateVal = dateFormater.parse(value);
					fieldAccessor.setFieldValue(fieldDefinition.getName(), dateVal);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Date field: {0}.{1} was set with: {2}", class1,
								fieldDefinition.getName(), dateVal));
					}
				}

			} catch (ParseException e) {
				logger.warn(MessageFormat.format("Unable to bind date field:{0}. {1}", dateField, e.getMessage()));
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
			// lazy creation - mostly not used
			if (fieldAccessor == null) {
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}

			if (!fieldDefinition.isEditable() || fieldDefinition.getJavaType() != Date.class) {
				continue;
			}

			DateFieldTypeDefinition fieldTypeDefinition = (DateFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			Assert.notNull(fieldTypeDefinition, "A field of type date is defined without @ScreenDateField annotation");

			String pattern = fieldTypeDefinition.getPattern();
			if (pattern == null) {
				continue;
			}

			TerminalField dateField = terminalSnapshot.getField(fieldDefinition.getPosition());

			if (!dateField.isEditable()) {
				continue;
			}

			SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));

			Date dateFieldValue = (Date)fieldAccessor.evaluateFieldValue(fieldDefinition.getName());

			if (dateFieldValue != null) {
				String dateStr = dateFormater.format(dateFieldValue);

				if (!dateStr.equals(dateField.getValue())) {
					dateField.setValue(dateStr);
					sendAction.getFields().add(dateField);
				}
			} else {
				if (fieldDefinition.getNullValue() != null) {
					dateField.setValue(fieldDefinition.getNullValue());
					sendAction.getFields().add(dateField);
				}
			}

		}
	}

}
