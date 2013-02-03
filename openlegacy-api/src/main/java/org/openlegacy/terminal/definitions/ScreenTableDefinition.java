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
package org.openlegacy.terminal.definitions;

import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

/**
 * Defines all the information required to manage terminal session tables including drill-down definitions Typically loaded from
 * 
 * {@link ScreenTable}, {@link ScreenTableDrilldown} & {@link ScreenColumn} annotations and stored within
 * {@link ScreenEntitiesRegistry}
 * 
 * @author Roi Mor
 */
@SuppressWarnings("rawtypes")
public interface ScreenTableDefinition extends TableDefinition<ScreenColumnDefinition> {

	int getStartRow();

	int getEndRow();

	int getRowGaps();

	TerminalAction getNextScreenAction();

	TerminalAction getPreviousScreenAction();

	DrilldownDefinition getDrilldownDefinition();

	Class<? extends TableCollector> getTableCollector();

	void setTableCollector(Class<? extends TableCollector> tableCollector);

	String getRowSelectionField();

	ScreenColumnDefinition getSelectionColumn();

	public interface ScreenColumnDefinition extends TableDefinition.ColumnDefinition {

		boolean isSelectionField();

		int getStartColumn();

		int getEndColumn();

		int getLength();

	}

	public interface DrilldownDefinition {

		Class<? extends RowFinder> getRowFinder();

		Class<? extends RowSelector> getRowSelector();

		Class<? extends RowComparator> getRowComparator();

		Class<? extends TableScroller> getTableScroller();

		Class<? extends TableScrollStopConditions> getTableScrollStopCondition();

		Class<? extends TableDrilldownPerformer> getDrilldownPerformer();

		void setRowFinder(Class<? extends RowFinder> rowFinder);

		void setRowComparator(Class<? extends RowComparator> rowComparator);

		void setRowSelector(Class<? extends RowSelector> rowSelector);

		void setTableScroller(Class<? extends TableScroller> tableScroller);

		void setTableScrollStopConditions(Class<? extends TableScrollStopConditions> tableScrollStopConditions);

		void setDrilldownPerformer(Class<? extends TableDrilldownPerformer> drilldownPerformer);
	}

}
