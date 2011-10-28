package org.openlegacy.terminal;

import org.openlegacy.HostEntityDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Map;

public interface ScreenEntityDefinition extends HostEntityDefinition {

	ScreenIdentification getScreenIdentification();

	Map<String, FieldMappingDefinition> getFieldMappingDefinitions();

}
