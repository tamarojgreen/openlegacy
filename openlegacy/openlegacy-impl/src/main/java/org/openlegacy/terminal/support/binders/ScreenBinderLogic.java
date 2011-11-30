package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
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
			Collection<FieldMappingDefinition> fieldMappingDefinitions) {

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalField terminalField = extractTerminalField(terminalSnapshot, fieldMappingDefinition);

			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isWritable(fieldName)) {
				String content = fieldFormatter.format(terminalField.getValue());
				fieldAccessor.setFieldValue(fieldName, content);

				fieldAccessor.setTerminalField(fieldName, terminalField);
			}
			ScreenPosition cursorPosition = terminalSnapshot.getCursorPosition();
			if (cursorPosition != null && cursorPosition.equals(fieldMappingDefinition.getScreenPosition())) {
				fieldAccessor.setFocusField(fieldMappingDefinition.getName());
			}

		}
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object screenPojo,
			Collection<FieldMappingDefinition> fieldMappingsDefinitions) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenPojo);

		List<TerminalField> modifiedfields = sendAction.getModifiedFields();

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			ScreenPosition fieldPosition = fieldMappingDefinition.getScreenPosition();
			String fieldName = fieldMappingDefinition.getName();
			Object value = fieldAccessor.getFieldValue(fieldName);

			TerminalField terminalField = terminalSnapshot.getField(fieldPosition);
			if (value != null) {
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

	private static TerminalField extractTerminalField(final TerminalSnapshot terminalSnapshot, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalSnapshot.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

}
