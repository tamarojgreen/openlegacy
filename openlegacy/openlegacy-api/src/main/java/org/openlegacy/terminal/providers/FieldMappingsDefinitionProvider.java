package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;

import java.util.Collection;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity
 * 
 */
public interface FieldMappingsDefinitionProvider extends DefinitionsProvider {

	Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity);

}
