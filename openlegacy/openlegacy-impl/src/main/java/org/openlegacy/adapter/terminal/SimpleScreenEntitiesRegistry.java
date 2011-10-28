package org.openlegacy.adapter.terminal;

import org.openlegacy.adapter.SimpleHostEntitiesRegistry;
import org.openlegacy.terminal.ScreenEntityDefinition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Collection;

/**
 * A simple implementation of a screen entities registry. Holds information collection from @ScreenEntity, @FieldMapping and more
 * 
 */
public class SimpleScreenEntitiesRegistry extends SimpleHostEntitiesRegistry<ScreenEntityDefinition> implements ScreenEntitiesRegistry {

	public ScreenEntityDefinition match(TerminalScreen hostScreen) {
		Collection<ScreenEntityDefinition> screenDefinitionsValues = getEntityDefinitions().values();
		for (ScreenEntityDefinition screenDefinition : screenDefinitionsValues) {
			ScreenIdentification screenIdentification = screenDefinition.getScreenIdentification();
			if (screenIdentification != null && screenIdentification.match(hostScreen)) {
				return screenDefinition;
			}
		}
		return null;
	}

}
