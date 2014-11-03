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

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.table.TerminalDrilldownAction;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

/**
 * Default terminal row selection implementation. Extract a single table definition for the given entity, fetch the table rows,
 * and marks the right row with the given actionValue
 * 
 * return the defined action to perform
 */
public class DefaultRowSelector<T> implements RowSelector<TerminalSession, T> {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Override
	public <D extends DrilldownAction<?>> void selectRow(TerminalSession terminalSession, T screenEntity, D drilldownAction,
			int rowNumber) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Entry<String, ScreenTableDefinition> tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, screenEntity.getClass());

		ScreenTableDefinition tableDefinitionVal = tableDefinition.getValue();
		String selectionField = tableDefinitionVal.getRowSelectionField();

		String tableFieldName = tableDefinition.getKey();
		List<?> rows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);

		Object row = rows.get(rowNumber);

		ScreenPojoFieldAccessor rowFieldAccessor = new SimpleScreenPojoFieldAccessor(row);

		if (selectionField != null) {
			rowFieldAccessor.setFieldValue(selectionField, drilldownAction.getActionValue());
		}
		int screenRowNumber = tableDefinitionVal.getStartRow() + rowNumber;
		int column = tableDefinitionVal.getColumnDefinitions().get(0).getStartColumn();
		fieldAccessor.setFieldValue(ScreenEntity.FOCUS_FIELD,
				SnapshotUtils.toAbsolutePosition(screenRowNumber, column, SimpleScreenSize.DEFAULT));

		terminalSession.doAction((TerminalDrilldownAction)drilldownAction, (ScreenEntity)screenEntity);
	}
}
