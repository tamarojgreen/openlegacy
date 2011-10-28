package org.openlegacy.terminal.spi;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.ScreenEntityDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends HostEntitiesRegistry<ScreenEntityDefinition> {

	void add(ScreenEntityDefinition screenEntityDefinition);

}
