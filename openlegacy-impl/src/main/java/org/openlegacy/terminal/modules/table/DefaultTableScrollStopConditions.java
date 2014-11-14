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
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

/**
 * Default terminal session stop conditions implementation. Check whether the current screen rows are not maximized or the
 * scrolling didn't switch any screen
 */
public class DefaultTableScrollStopConditions<T> implements TableScrollStopConditions<T> {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	/**
	 * Check if the screen after the scroll contains all the rows in the screen before
	 */
	@Override
	public boolean shouldStop(T beforeScrollEntity, T afterScrollEntity) {
		List<?> beforeScrollRows = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, beforeScrollEntity);
		List<?> afterScrollRows = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, afterScrollEntity);
		if (afterScrollRows != null && beforeScrollRows.containsAll(afterScrollRows)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldStop(T currentEntity) {
		Entry<String, ScreenTableDefinition> tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, currentEntity.getClass());

		ScreenPojoFieldAccessor screenPojoFieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
		List<?> rows = (List<?>)screenPojoFieldAccessor.getFieldValue(tableDefinition.getKey());

		if (tableDefinition.getValue().getMaxRowsCount() > rows.size()) {
			return true;
		}

		return false;
	}

}
