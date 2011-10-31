package org.openlegacy.terminal.support;

import org.openlegacy.support.SimpleHostEntitiesRegistry;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple implementation of a screen entities registry. Holds information collection from @ScreenEntity, @FieldMapping and more
 * 
 */
public class DefaultScreenEntitiesRegistry extends SimpleHostEntitiesRegistry<ScreenEntityDefinition, FieldMappingDefinition> implements ScreenEntitiesRegistry {

	public ScreenEntityDefinition match(TerminalScreen hostScreen) {
		Collection<ScreenEntityDefinition> screenDefinitionsValues = getEntitiesDefinitions().values();

		// sort the screen definitions by identifiers count
		List<ScreenEntityDefinition> screenDefinitionsList = new ArrayList<ScreenEntityDefinition>(screenDefinitionsValues);
		Collections.sort(screenDefinitionsList, new Comparator<ScreenEntityDefinition>() {

			public int compare(ScreenEntityDefinition o1, ScreenEntityDefinition o2) {
				return o2.getScreenIdentification().getScreenIdentifiers().size()
						- o1.getScreenIdentification().getScreenIdentifiers().size();
			}
		});

		for (ScreenEntityDefinition screenDefinition : screenDefinitionsList) {
			ScreenIdentification screenIdentification = screenDefinition.getScreenIdentification();
			if (screenIdentification != null && screenIdentification.match(hostScreen)) {
				return screenDefinition;
			}
		}
		return null;
	}

}
