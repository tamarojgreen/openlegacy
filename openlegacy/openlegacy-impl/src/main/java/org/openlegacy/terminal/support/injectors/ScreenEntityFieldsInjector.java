package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;

import java.util.Collection;

import javax.inject.Inject;

public class ScreenEntityFieldsInjector implements ScreenEntityDataInjector {

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen) {

		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntityClass);

		InjectorUtil.injectFields(fieldAccessor, terminalScreen, fieldMappingDefinitions, fieldFormatter);
	}

	public FieldMappingsDefinitionProvider getDefinitionsProvider() {
		return fieldMappingsProvider;
	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}
}
