package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.TableDefinition;

import java.util.Collection;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity
 * 
 */
public interface TablesDefinitionProvider extends DefinitionsProvider {

	Collection<TableDefinition> getTableDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity);

}
