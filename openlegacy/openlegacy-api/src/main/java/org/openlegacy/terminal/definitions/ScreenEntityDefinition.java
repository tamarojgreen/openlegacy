package org.openlegacy.terminal.definitions;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.List;
import java.util.Map;

public interface ScreenEntityDefinition extends EntityDefinition<FieldMappingDefinition> {

	ScreenIdentification getScreenIdentification();

	NavigationDefinition getNavigationDefinition();

	/**
	 * field name -> table definition
	 * 
	 * @return
	 */
	Map<String, TableDefinition> getTableDefinitions();

	/**
	 * field name -> part defintion
	 * 
	 * @return
	 */
	Map<String, ScreenPartEntityDefinition> getPartsDefinitions();

	List<ActionDefinition> getActions();

	TerminalSnapshot getSnapshot();
}
