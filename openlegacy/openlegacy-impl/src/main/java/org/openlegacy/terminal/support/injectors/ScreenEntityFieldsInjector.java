package org.openlegacy.terminal.support.injectors;

import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;

import java.util.Collection;

import javax.inject.Inject;

public class ScreenEntityFieldsInjector implements ScreenEntityDataInjector<FieldMappingsDefinitionProvider> {

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen,
			boolean deep) {

		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntityClass);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMappingDefinition);

			String fieldName = fieldMappingDefinition.getName();
			if (fieldAccessor.isWritableProperty(fieldName)) {
				fieldAccessor.setTerminalField(fieldName, terminalField);
			}

		}
	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

	public FieldMappingsDefinitionProvider DefinitionsProvider() {
		return fieldMappingsProvider;
	}

}
