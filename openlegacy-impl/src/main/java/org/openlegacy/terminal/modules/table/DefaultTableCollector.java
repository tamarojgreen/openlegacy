/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.table.ScreenTableCollector;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

public class DefaultTableCollector<T> implements ScreenTableCollector<T> {

	@Inject
	private transient ApplicationContext applicationContext;
	private int maxScreens = 50;

	@Override
	public List<T> collectAll(TerminalSession terminalSession, Class<?> screenEntityClass, Class<T> rowClass) {
		return collectAllInner(terminalSession, screenEntityClass, rowClass, maxScreens);
	}

	@Override
	public List<T> collectOne(TerminalSession terminalSession, Class<?> screenEntityClass, Class<T> rowClass) {
		return collectAllInner(terminalSession, screenEntityClass, rowClass, 1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<T> collectAllInner(TerminalSession terminalSession, Class<?> screenEntityClass, Class<T> rowClass,
			int numberOfScreens) {

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		TableScrollStopConditions tableScrollStopConditions = SpringUtil.getBean(applicationContext,
				TableScrollStopConditions.class);

		ScreenEntityDefinition screenEntityDefintion = screenEntitiesRegistry.get(screenEntityClass);
		Map<String, ScreenTableDefinition> tableDefinitionsMap = screenEntityDefintion.getTableDefinitions();
		Set<Entry<String, ScreenTableDefinition>> tableDefinitionEntries = tableDefinitionsMap.entrySet();

		Entry<String, ScreenTableDefinition> matchingDefinitionEntry = null;

		for (Entry<String, ScreenTableDefinition> tableDefinitionEntry : tableDefinitionEntries) {
			if (tableDefinitionEntry.getValue().getTableClass() == rowClass) {
				matchingDefinitionEntry = tableDefinitionEntry;
				break;
			}
		}

		if (matchingDefinitionEntry == null) {
			throw (new IllegalArgumentException(MessageFormat.format("Could not find table of type {0} in {1}", rowClass,
					screenEntityDefintion)));
		}

		ScreenTableDefinition matchingTableDefinition = matchingDefinitionEntry.getValue();
		int rowsCount = (matchingTableDefinition.getEndRow() - matchingTableDefinition.getStartRow() + 1)
				/ matchingTableDefinition.getRowGaps();

		List<T> allRows = new ArrayList<T>();

		List<T> rows = null;
		boolean cont = true;

		Object screenEntity = terminalSession.getEntity(screenEntityClass);
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		int screensCollected = 1;
		while (cont) {

			rows = (List<T>)fieldAccessor.getFieldValue(matchingDefinitionEntry.getKey());
			if (rows.size() > 0 && allRows.contains(rows.get(0))) {
				cont = false;
			} else {
				allRows.addAll(rows);
			}

			// more rows exists
			if (allRows.size() < rowsCount) {
				cont = false;
			} else {
				if (numberOfScreens <= screensCollected || tableScrollStopConditions.shouldStop(screenEntity)) {
					break;
				}
				screenEntity = terminalSession.doAction(matchingTableDefinition.getNextScreenAction());
				screensCollected++;
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}
		}

		return allRows;
	}

	@Override
	public List<T> collect(TerminalSession terminalSession, Class<?> screenEntityClass, Class<T> rowClass, int numberOfScreens) {
		return collectAllInner(terminalSession, screenEntityClass, rowClass, numberOfScreens);
	}

	public void setMaxScreens(int maxScreens) {
		this.maxScreens = maxScreens;
	}

}
