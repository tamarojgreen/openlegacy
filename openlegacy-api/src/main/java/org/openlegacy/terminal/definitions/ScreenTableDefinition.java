package org.openlegacy.terminal.definitions;

import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;

/**
 * Defines all the information required to manage terminal session tables including drill-down definitions Typically loaded from
 * 
 * @ScreenTable, @ScreenTableDrilldown & @ScreenColumn annotations and stored within <code>ScreenEntitiesRegistry</code>
 * 
 */
@SuppressWarnings("rawtypes")
public interface ScreenTableDefinition extends TableDefinition<ScreenColumnDefinition> {

	int getStartRow();

	int getEndRow();

	TerminalAction getNextScreenAction();

	TerminalAction getPreviousScreenAction();

	DrilldownDefinition getDrilldownDefinition();

	Class<? extends TableCollector> getTableCollector();

	void setTableCollector(Class<? extends TableCollector> tableCollector);

	String getRowSelectionField();

	public interface ScreenColumnDefinition extends TableDefinition.ColumnDefinition {

		boolean isSelectionField();

		int getStartColumn();

		int getEndColumn();

		boolean isEditable();

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
