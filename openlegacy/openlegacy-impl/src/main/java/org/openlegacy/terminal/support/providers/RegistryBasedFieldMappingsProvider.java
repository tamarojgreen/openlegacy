package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;

import java.util.Collection;

import javax.inject.Inject;

/**
 * A FieldMappingsDefinitionProvider based on open legacy @FieldMapping annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedFieldMappingsProvider implements FieldMappingsDefinitionProvider {

	@Inject
	private DefaultScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot, Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getFieldsDefinitions().values();
	}
}
