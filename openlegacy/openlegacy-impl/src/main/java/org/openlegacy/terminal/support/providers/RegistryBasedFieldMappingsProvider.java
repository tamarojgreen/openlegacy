package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.support.SimpleScreenEntitiesRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * A FieldMappingsDefinitionProvider based on open legacy @FieldMapping annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedFieldMappingsProvider implements FieldMappingsDefinitionProvider {

	@Autowired
	private SimpleScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getFieldDefinitions().values();
	}
}
