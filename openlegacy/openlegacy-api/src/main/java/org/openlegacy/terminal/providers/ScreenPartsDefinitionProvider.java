package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;

import java.util.Collection;

/**
 * FieldMapping meta-data provider purpose is to return mappings for a given screenEntity
 * 
 */
public interface ScreenPartsDefinitionProvider extends DefinitionsProvider {

	Collection<ScreenPartEntityDefinition> getScreenPartsDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity);

}
