package org.openlegacy.terminal.spi;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.FieldMappingDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends HostEntitiesRegistry {

	void addScreenIdentification(ScreenIdentification screenIdentification);

	void addFieldMappingDefinition(Class<?> screenEntity, FieldMappingDefinition fieldMappingDefinition);
}
