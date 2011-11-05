package org.openlegacy.terminal.definitions;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Map;

public interface ScreenEntityDefinition extends HostEntityDefinition<FieldMappingDefinition> {

	ScreenIdentification getScreenIdentification();

	/**
	 * Map of field name -> child screen definition
	 * 
	 * @return
	 */
	Map<String, ChildScreenDefinition> getChildScreenDefinitions();

	NavigationDefinition getNavigationDefinition();

	/**
	 * Row class -> table definition
	 * 
	 * @return
	 */
	Map<Class<?>, TableDefinition> getTableDefinitions();
}
