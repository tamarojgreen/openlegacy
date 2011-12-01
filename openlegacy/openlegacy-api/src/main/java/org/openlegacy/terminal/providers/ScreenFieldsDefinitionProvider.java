package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.Collection;

/**
 * Field mapping provider purpose is to return mappings for a given screenEntity. Implementation depends on the legacy provider
 * 
 */
public interface ScreenFieldsDefinitionProvider extends DefinitionsProvider {

	Collection<ScreenFieldDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot, Class<?> screenEntityClass);

}
