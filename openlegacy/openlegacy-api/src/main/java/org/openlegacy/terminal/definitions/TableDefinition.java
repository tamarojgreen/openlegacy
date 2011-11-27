package org.openlegacy.terminal.definitions;

import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.actions.TerminalAction;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface TableDefinition {

	Class<?> getTableClass();

	String getTableEntityName();

	int getStartRow();

	int getEndRow();

	List<ColumnDefinition> getColumnDefinitions();

	ColumnDefinition getColumnDefinition(String fieldName);

	TerminalAction getNextScreenAction();

	TerminalAction getPreviousScreenAction();

	List<String> getKeyFieldNames();

	int getMaxRowsCount();

	DrilldownDefinition getDrilldownDefinition();

	Class<? extends TableCollector> getTableCollector();

	void setTableCollector(Class<? extends TableCollector> tableCollector);

	boolean isScrollable();

	String getRowSelectionField();

	public interface ColumnDefinition {

		String getName();

		boolean isKey();

		boolean isSelectionField();

		int getStartColumn();

		int getEndColumn();

		boolean isEditable();

		String getDisplayName();

		String getSampleValue();
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
