package org.openlegacy.terminal.support.binders;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Component
public class ScreenEntityFieldsBinder implements ScreenEntityBinder {

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private FieldComparator fieldComparator;

	@Inject
	private FieldFormatter fieldFormatter;

	private final static Log logger = LogFactory.getLog(ScreenEntityFieldsBinder.class);

	public void populateEntity(Object screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		fieldAccessor.setTerminalScreen(terminalScreen);

		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());
		InjectorUtil.injectFields(fieldAccessor, terminalScreen, fieldMappingDefinitions, fieldFormatter);
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalScreen terminalScreen, Object entity) {

		if (entity == null) {
			return;
		}

		Assert.isTrue(entity instanceof ScreenEntity, "screen entity must implement ScreenEntity interface");

		ScreenEntity screenEntity = (ScreenEntity)entity;

		Collection<FieldMappingDefinition> fieldMappingsDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());

		if (fieldMappingsDefinitions == null) {
			return;
		}

		List<TerminalField> modifiedfields = sendAction.getModifiedFields();

		String focusField = screenEntity.getFocusField();

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			ScreenPosition fieldPosition = fieldMappingDefinition.getScreenPosition();
			String fieldName = fieldMappingDefinition.getName();
			Object value = fieldAccessor.getFieldValue(fieldName);

			TerminalField terminalField = terminalScreen.getField(fieldPosition);
			if (value != null) {
				boolean fieldModified = fieldComparator.isFieldModified(screenEntity, fieldName, terminalField.getValue(), value);
				if (fieldModified) {
					if (fieldMappingDefinition.isEditable()) {
						terminalField.setValue(value.toString());
						modifiedfields.add(terminalField);
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Field {0} was set with value \"{1}\" to send fields for screen entity {2}", fieldName,
									value, screenEntity));
						}
					} else {
						throw (new SendActionException(MessageFormat.format(
								"Field {0} in screen {1} was modified with value {2}, but is not defined as editable", fieldName,
								screenEntity, value)));

					}
				}
			}
			if (fieldName.equals(focusField)) {
				sendAction.setCursorPosition(fieldPosition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Cursor was set at position {0} from field {1}", fieldPosition, focusField));
				}
			}

		}

		if (!StringUtils.isEmpty(focusField) && sendAction.getCursorPosition() == null) {
			throw (new SendActionException(MessageFormat.format("Cursor field {0} was not found in screen {1}", focusField,
					ProxyUtil.getOriginalClass(screenEntity.getClass()))));
		}
	}
}
