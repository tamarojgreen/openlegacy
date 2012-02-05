package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.definitions.ScreenTableDefinition;

import java.util.Map;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity
 * 
 */
public interface TablesDefinitionProvider extends DefinitionsProvider {

	Map<String, ScreenTableDefinition> getTableDefinitions(Class<?> screenEntityClass);

}
