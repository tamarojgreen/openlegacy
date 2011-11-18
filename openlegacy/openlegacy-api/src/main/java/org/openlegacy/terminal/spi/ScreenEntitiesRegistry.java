package org.openlegacy.terminal.spi;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends HostEntitiesRegistry<ScreenEntityDefinition, FieldMappingDefinition> {

	void addPart(ScreenPartEntityDefinition screenPartEntityDefinition);

	ScreenPartEntityDefinition getPart(Class<?> containingClass);

	void addTable(TableDefinition tableDefinition);

	TableDefinition getTable(Class<?> containingClass);
}
