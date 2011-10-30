package org.openlegacy.terminal.definitions;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Map;

public interface ScreenEntityDefinition extends HostEntityDefinition {

	ScreenIdentification getScreenIdentification();

	/**
	 * Map of field name -> field mapping definition
	 * 
	 * @return
	 */
	Map<String, FieldMappingDefinition> getFieldMappingDefinitions();

	/**
	 * Map of field name -> child screen definition
	 * 
	 * @return
	 */
	Map<String, ChildScreenDefinition> getChildScreenDefinitions();

}
