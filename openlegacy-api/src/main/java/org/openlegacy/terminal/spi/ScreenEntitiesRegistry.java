package org.openlegacy.terminal.spi;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends EntitiesRegistry<ScreenEntityDefinition, ScreenFieldDefinition> {

	void addPart(ScreenPartEntityDefinition screenPartEntityDefinition);

	ScreenPartEntityDefinition getPart(Class<?> containingClass);

	void addTable(ScreenTableDefinition tableDefinition);

	ScreenTableDefinition getTable(Class<?> containingClass);
}
