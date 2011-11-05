package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;

import java.util.Collection;

import javax.inject.Inject;

/**
 * A ChildScreensDefinitionProvider based on open legacy @ChildScreenEntity annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedTablesDefinitionProvider implements TablesDefinitionProvider {

	@Inject
	private DefaultScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<TableDefinition> getTableDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getTableDefinitions().values();
	}
}
