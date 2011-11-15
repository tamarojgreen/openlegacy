package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;

import java.util.Collection;

public class InjectorUtil {

	public static void injectFields(ScreenPojoFieldAccessor fieldAccessor, TerminalScreen terminalScreen,
			Collection<FieldMappingDefinition> fieldMappingDefinitions, FieldFormatter fieldFormatter) {
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

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

}
