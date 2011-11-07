package org.openlegacy.terminal.definitions;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Map;

public interface ScreenEntityDefinition extends HostEntityDefinition<FieldMappingDefinition> {

	ScreenIdentification getScreenIdentification();

	NavigationDefinition getNavigationDefinition();

	/**
	 * Row class -> table definition
	 * 
	 * @return
	 */
	Map<Class<?>, TableDefinition> getTableDefinitions();

	/**
	 * field name -> part defintion
	 * 
	 * @return
	 */
	Map<String, ScreenPartEntityDefinition> getPartsDefinitions();
}
