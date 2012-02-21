package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;

import java.util.Map;

import javax.inject.Inject;

/**
 * A table definition provider based on open legacy @ScreenTable annotation
 * 
 */
public class RegistryBasedTablesDefinitionProvider implements TablesDefinitionProvider {

	@Inject
	private DefaultScreenEntitiesRegistry screenEntitiesRegistry;

	public Map<String, ScreenTableDefinition> getTableDefinitions(Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getTableDefinitions();
	}
}
