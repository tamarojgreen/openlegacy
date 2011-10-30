package org.openlegacy.terminal.spi;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends HostEntitiesRegistry<ScreenEntityDefinition, FieldMappingDefinition> {

	void add(ScreenEntityDefinition screenEntityDefinition);

}
