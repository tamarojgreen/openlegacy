package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@Component
public class ScreenBinderLogic {

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private FieldComparator fieldComparator;

	private final static Log logger = LogFactory.getLog(ScreenBinderLogic.class);

	public void populatedFields(ScreenPojoFieldAccessor fieldAccessor, TerminalScreen terminalScreen,
			Collection<FieldMappingDefinition> fieldMappingDefinitions) {

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMappingDefinition);

			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isEditable(fieldName)) {
				String content = fieldFormatter.format(terminalField.getValue());
				fieldAccessor.setFieldValue(fieldName, content);

				fieldAccessor.setTerminalField(fieldName, terminalField);
			}

		}
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalScreen terminalScreen, Object screenPojo,
			Collection<FieldMappingDefinition> fieldMappingsDefinitions) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenPojo);

		List<TerminalField> modifiedfields = sendAction.getModifiedFields();

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			ScreenPosition fieldPosition = fieldMappingDefinition.getScreenPosition();
			String fieldName = fieldMappingDefinition.getName();
			Object value = fieldAccessor.getFieldValue(fieldName);

			TerminalField terminalField = terminalScreen.getField(fieldPosition);
			if (value != null) {
				boolean fieldModified = fieldComparator.isFieldModified(screenPojo, fieldName, terminalField.getValue(), value);
				if (fieldModified) {
					if (fieldMappingDefinition.isEditable()) {
						terminalField.setValue(value.toString());
						modifiedfields.add(terminalField);
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Field {0} was set with value \"{1}\" to send fields for screen entity {2}", fieldName,
									value, screenPojo));
						}
					} else {
						throw (new SendActionException(MessageFormat.format(
								"Field {0} in screen {1} was modified with value {2}, but is not defined as editable", fieldName,
								screenPojo, value)));

					}
				}
			}
			if (screenPojo instanceof ScreenEntity) {
				ScreenEntity screenEntity = (ScreenEntity)screenPojo;
				if (fieldName.equals(screenEntity.getFocusField())) {
					sendAction.setCursorPosition(fieldPosition);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Cursor was set at position {0} from field {1}", fieldPosition,
								screenEntity.getFocusField()));
					}
				}
			}

		}
	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

}
