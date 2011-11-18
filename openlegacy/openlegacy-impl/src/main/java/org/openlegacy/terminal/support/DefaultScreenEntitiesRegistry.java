package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.support.AbstractHostEntitiesRegistry;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple implementation of a screen entities registry. Holds information collection from @ScreenEntity, @FieldMapping and more
 * 
 */
public class DefaultScreenEntitiesRegistry extends AbstractHostEntitiesRegistry<ScreenEntityDefinition, FieldMappingDefinition> implements ScreenEntitiesRegistry {

	private final Map<Class<?>, ScreenPartEntityDefinition> screenPartDefinitions = new HashMap<Class<?>, ScreenPartEntityDefinition>();
	private final Map<Class<?>, TableDefinition> tableDefinitions = new HashMap<Class<?>, TableDefinition>();

	private final static Log logger = LogFactory.getLog(DefaultScreenEntitiesRegistry.class);

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

	public void addPart(ScreenPartEntityDefinition screenPartEntityDefinition) {
		screenPartDefinitions.put(screenPartEntityDefinition.getPartClass(), screenPartEntityDefinition);
	}

	public ScreenPartEntityDefinition getPart(Class<?> containingClass) {
		return screenPartDefinitions.get(containingClass);
	}

	public void addTable(TableDefinition tableDefinition) {
		tableDefinitions.put(tableDefinition.getTableClass(), tableDefinition);
	}

	public TableDefinition getTable(Class<?> containingClass) {
		return tableDefinitions.get(containingClass);
	}

	@Override
	public void clear() {
		super.clear();

		screenPartDefinitions.clear();
		tableDefinitions.clear();

		if (logger.isDebugEnabled()) {
			logger.debug("Cleared screen entities registry");
		}

	}
}
