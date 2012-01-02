package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.exceptions.TerminalActionException;
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

	public void populatedFields(ScreenPojoFieldAccessor fieldAccessor, TerminalSnapshot terminalSnapshot,
			Collection<ScreenFieldDefinition> fieldMappingDefinitions) {

		for (ScreenFieldDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalPosition position = fieldMappingDefinition.getPosition();
			TerminalField terminalField = terminalSnapshot.getField(position);
			if (terminalField == null) {
				continue;
			}
			TerminalRow row = terminalSnapshot.getRow(position.getRow());
			String text = terminalField.getValue();
			if (fieldMappingDefinition.getLength() > 0) {
				text = row.getText(position.getColumn(), fieldMappingDefinition.getLength());
			}
			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isWritable(fieldName)) {
				String content = fieldFormatter.format(text);
				fieldAccessor.setFieldValue(fieldName, content);

				fieldAccessor.setTerminalField(fieldName, terminalField);
			}
			TerminalPosition cursorPosition = terminalSnapshot.getCursorPosition();
			if (cursorPosition != null && cursorPosition.equals(position)) {
				fieldAccessor.setFocusField(fieldMappingDefinition.getName());
			}

		}
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object screenPojo,
			Collection<ScreenFieldDefinition> fieldMappingsDefinitions) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenPojo);

		List<TerminalField> modifiedfields = sendAction.getModifiedFields();

		for (ScreenFieldDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			if (!fieldMappingDefinition.isEditable()) {
				continue;
			}
			TerminalPosition fieldPosition = fieldMappingDefinition.getPosition();
			String fieldName = fieldMappingDefinition.getName();

			if (!fieldAccessor.isExists(fieldName)) {
				continue;
			}
			Object value = fieldAccessor.getFieldValue(fieldName);

			TerminalField terminalField = terminalSnapshot.getField(fieldPosition);
			if (terminalField.isEditable() && value != null) {
				boolean fieldModified = fieldComparator.isFieldModified(screenPojo, fieldName, terminalField.getValue(), value);
				if (fieldModified) {
					if (fieldMappingDefinition.isEditable()) {
						terminalField.setValue(value.toString());
						modifiedfields.add(terminalField);
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Field {0} was set with value \"{1}\" to send fields for screen {2}", fieldName, value,
									screenPojo.getClass()));
						}
					} else {
						throw (new TerminalActionException(MessageFormat.format(
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

}
