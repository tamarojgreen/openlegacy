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
package org.openlegacy.terminal.support.binders;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Component
public class DateFieldsBinder implements ScreenEntityBinder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	private final static Log logger = LogFactory.getLog(DateFieldsBinder.class);

	private String centuryPrefix = "20";

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
			if (fieldTypeDefinition.getPattern() != null) {
				continue;
			}
			int row = fieldDefinition.getPosition().getRow();

			TerminalField dayField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getDayColumn()));
			TerminalField monthField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getMonthColumn()));
			TerminalField yearField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getYearColumn()));

			String dayValue = dayField.getValue();
			String monthValue = monthField.getValue();
			String yearValue = yearField.getValue();
			if (yearField.getLength() == 2) {
				yearValue = centuryPrefix + yearValue;
			}
			// TOOD - currently only when all 3 date fields are full date is initialized.
			// does it mean something if only 1-2 has value?
			if (NumberUtils.isNumber(dayValue) && NumberUtils.isNumber(monthValue) && NumberUtils.isNumber(yearValue)) {
				// month is 0 based in Calendar
				Calendar calendar = new GregorianCalendar(Integer.valueOf(yearValue), Integer.valueOf(monthValue) - 1,
						Integer.valueOf(dayValue));
				Date date = calendar.getTime();
				fieldAccessor.setFieldValue(fieldDefinition.getName(), date);

				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Date field: {0}.{1} was set with: {2}", class1, fieldDefinition.getName(),
							date));
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Unable to bind date field: {0}.{1}. Year={2}, month={3}, day={4}", class1,
							fieldDefinition.getName(), yearValue, monthValue, dayValue));
				}
			}

		}
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object entity) {

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

			if (fieldDefinition.getJavaType() != Date.class) {
				continue;
			}

			DateFieldTypeDefinition fieldTypeDefinition = (DateFieldTypeDefinition)fieldDefinition.getFieldTypeDefinition();
			Assert.notNull(fieldTypeDefinition, "A field of type date is defined without @ScreenDateField annotation");

			if (fieldTypeDefinition.getPattern() != null) {
				continue;
			}
			int row = fieldDefinition.getPosition().getRow();

			TerminalField dayField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getDayColumn()));
			TerminalField monthField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getMonthColumn()));
			TerminalField yearField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(row,
					fieldTypeDefinition.getYearColumn()));

			Date dateFieldValue = (Date)fieldAccessor.getFieldValue(fieldDefinition.getName());

			Calendar calender = Calendar.getInstance();
			calender.setTime(dateFieldValue);

			List<TerminalField> modifiedFields = sendAction.getModifiedFields();
			if (dayField != null) {
				String value = StringUtil.appendLeftZeros(calender.get(Calendar.DAY_OF_MONTH), dayField.getLength());
				setValue(dayField, modifiedFields, value);
			}
			if (monthField != null) {
				// MONTH is 0 based in calendar
				String value = StringUtil.appendLeftZeros(calender.get(Calendar.MONTH) + 1, monthField.getLength());
				setValue(monthField, modifiedFields, value);
			}
			if (yearField != null) {
				String value = StringUtil.appendLeftZeros(calender.get(Calendar.YEAR), yearField.getLength());
				setValue(yearField, modifiedFields, value);
			}
		}
	}

	private static void setValue(TerminalField field, List<TerminalField> modifiedFields, String value) {
		if (!value.equals(field.getValue())) {
			field.setValue(value);
			modifiedFields.add(field);
		}
	}

	/**
	 * 
	 * @param centuryPrefix
	 *            Used in case of 2 field length host field. default to "20". Avoid 2100 bug.
	 * 
	 */
	public void setCenturyPrefix(String centuryPrefix) {
		this.centuryPrefix = centuryPrefix;
	}
}
