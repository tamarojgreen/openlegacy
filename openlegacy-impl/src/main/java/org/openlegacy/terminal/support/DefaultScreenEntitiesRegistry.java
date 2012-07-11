/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of a screen entities registry. Holds information collection from @ScreenEntity, @FieldMapping and more
 * 
 */
public class DefaultScreenEntitiesRegistry extends AbstractEntitiesRegistry<ScreenEntityDefinition, ScreenFieldDefinition> implements ScreenEntitiesRegistry {

	private final Map<Class<?>, ScreenPartEntityDefinition> screenPartDefinitions = new HashMap<Class<?>, ScreenPartEntityDefinition>();
	private final Map<Class<?>, ScreenTableDefinition> tableDefinitions = new HashMap<Class<?>, ScreenTableDefinition>();
	private ArrayList<ScreenEntityDefinition> sortedScreenDefinitions;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntitiesRegistry.class);

	public ScreenEntityDefinition match(TerminalSnapshot terminalSnapshot) {
		Collection<ScreenEntityDefinition> screenDefinitionsValues = getEntitiesDefinitions();

		if (sortedScreenDefinitions == null) {
			initSortedScreenEntities(screenDefinitionsValues);
		}

		for (ScreenEntityDefinition screenDefinition : sortedScreenDefinitions) {
			ScreenIdentification screenIdentification = screenDefinition.getScreenIdentification();
			if (screenIdentification != null && screenIdentification.match(terminalSnapshot)) {
				return screenDefinition;
			}
		}
		return null;
	}

	private void initSortedScreenEntities(Collection<ScreenEntityDefinition> screenDefinitionsValues) {
		// sort the screen definitions by window, identifiers count
		sortedScreenDefinitions = new ArrayList<ScreenEntityDefinition>(screenDefinitionsValues);
		Collections.sort(sortedScreenDefinitions, new Comparator<ScreenEntityDefinition>() {

			public int compare(ScreenEntityDefinition o1, ScreenEntityDefinition o2) {
				if (o1.isWindow() == o2.isWindow()) {
					return o2.getScreenIdentification().getScreenIdentifiers().size()
							- o1.getScreenIdentification().getScreenIdentifiers().size();
				}
				if (o1.isWindow()) {
					return -1;
				}
				if (o2.isWindow()) {
					return 1;
				}
				return 0;
			}
		});
	}

	public void addPart(ScreenPartEntityDefinition screenPartEntityDefinition) {
		screenPartDefinitions.put(screenPartEntityDefinition.getPartClass(), screenPartEntityDefinition);
	}

	public ScreenPartEntityDefinition getPart(Class<?> containingClass) {
		return screenPartDefinitions.get(containingClass);
	}

	public void addTable(ScreenTableDefinition tableDefinition) {
		tableDefinitions.put(tableDefinition.getTableClass(), tableDefinition);
	}

	public ScreenTableDefinition getTable(Class<?> containingClass) {
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
