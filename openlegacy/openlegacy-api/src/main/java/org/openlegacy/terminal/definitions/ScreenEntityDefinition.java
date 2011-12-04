package org.openlegacy.terminal.definitions;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.List;
import java.util.Map;

/**
 * Defines a screen entity definitions stored in the <code>ScreenEntitiesRegistry</code> Typically collected from @ScreenEntity,
 * 
 * @ScreenIdentifiers, @ScreenNavigation, @ScreenActions, @FieldMapping annotations
 * 
 */
public interface ScreenEntityDefinition extends EntityDefinition<ScreenFieldDefinition> {

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

	boolean isWindow();
}
