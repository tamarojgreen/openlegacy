package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DateFieldsPatternBinder implements ScreenEntityBinder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	private final static Log logger = LogFactory.getLog(DateFieldsBinder.class);

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

			TerminalField dateField = terminalSnapshot.getField(fieldDefinition.getPosition());
			SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));

			try {
				Date dateVal = dateFormater.parse(dateField.getValue());
				fieldAccessor.setFieldValue(fieldDefinition.getName(), dateVal);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Date field: {0}.{1} was set with: {2}", class1, fieldDefinition.getName(),
							dateVal));
				}
			} catch (ParseException e) {
				throw (new RegistryException(MessageFormat.format("Unable to bind date field:{0}", dateField), e));
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

			String pattern = fieldTypeDefinition.getPattern();
			if (pattern == null) {
				continue;
			}

			TerminalField dateField = terminalSnapshot.getField(fieldDefinition.getPosition());

			SimpleDateFormat dateFormater = new SimpleDateFormat(pattern, new Locale(fieldTypeDefinition.getLocale()));

			Date dateFieldValue = (Date)fieldAccessor.getFieldValue(fieldDefinition.getName());

			Calendar calender = Calendar.getInstance();
			calender.setTime(dateFieldValue);

			if (dateFieldValue != null) {
				String dateStr = dateFormater.format(dateFieldValue);

				if (!dateStr.equals(dateField.getValue())) {
					dateField.setValue(dateStr);
					sendAction.getModifiedFields().add(dateField);
				}
			}

		}
	}

}
