package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;

import java.util.Collection;

public class InjectorUtil {

	public static void injectFields(ScreenEntityFieldAccessor fieldAccessor, TerminalScreen terminalScreen,
			Collection<FieldMappingDefinition> fieldMappingDefinitions, FieldFormatter fieldFormatter) {
		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMappingDefinition);

			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isEditable(fieldName)) {
				fieldAccessor.setTerminalField(fieldName, terminalField, fieldFormatter);
			}

		}
	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

}
