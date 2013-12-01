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
package org.openlegacy.terminal.modules.table;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Table utilities for retrieving various table entity definitions
 * 
 */
public class ScrollableTableUtil {

	/**
	 * Retrieve a table entity definition on a screen which is scroll-able. Only a single scroll-able table is allowed per screen
	 * 
	 * @param tablesDefinitionProvider
	 * @param screenEntityClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Entry<String, ScreenTableDefinition> getSingleScrollableTableDefinition(
			TablesDefinitionProvider tablesDefinitionProvider, Class<?> screenEntityClass) {

		Map<String, ScreenTableDefinition> tablesDefinitions = tablesDefinitionProvider.getTableDefinitions(screenEntityClass);
		Collection<Entry<String, ScreenTableDefinition>> tablesDefinitionEntries = tablesDefinitions.entrySet();

		if (tablesDefinitions.size() == 0) {
			return null;
		}
		if (tablesDefinitions.size() > 1) {
			Entry<String, ScreenTableDefinition> matchingEntry = null;
			for (Entry<String, ScreenTableDefinition> tablesDefinitionEntry : tablesDefinitionEntries) {

				if (tablesDefinitionEntry.getValue().isScrollable()) {
					if (matchingEntry != null) {
						throw (new RegistryException("Only a single scrollable table can be defined in a screen."
								+ screenEntityClass));
					}
					matchingEntry = tablesDefinitionEntry;
				}
			}
			return matchingEntry;
		}
		return (Entry<String, ScreenTableDefinition>)tablesDefinitions.entrySet().toArray()[0];

	}

	public static List<?> getSingleScrollableTable(TablesDefinitionProvider tablesDefinitionProvider, Object screenEntity) {
		Entry<String, ScreenTableDefinition> tableDefinition = getSingleScrollableTableDefinition(tablesDefinitionProvider,
				screenEntity.getClass());

		ScreenPojoFieldAccessor screenPojoFieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
		List<?> rows = (List<?>)screenPojoFieldAccessor.getFieldValue(tableDefinition.getKey());
		return rows;

	}

	public static String getRowSelectionField(ScreenTableDefinition tableDefinition) {
		List<ScreenColumnDefinition> columns = tableDefinition.getColumnDefinitions();
		String selectionField = null;
		for (ScreenColumnDefinition columnDefinition : columns) {
			if (columnDefinition.isSelectionField()) {
				Assert.isNull(selectionField,
						"Table can contain only a single selection field:" + tableDefinition.getTableEntityName());
				selectionField = columnDefinition.getName();
			}
		}
		return selectionField;
	}

}
