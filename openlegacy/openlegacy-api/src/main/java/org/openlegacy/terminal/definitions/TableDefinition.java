package org.openlegacy.terminal.definitions;

import org.openlegacy.HostAction;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;

import java.util.List;

public interface TableDefinition {

	Class<?> getTableClass();

	int getStartRow();

	int getEndRow();

	List<ColumnDefinition> getColumnDefinitions();

	ColumnDefinition getColumnDefinition(String fieldName);

	HostAction getNextScreenAction();

	HostAction getPreviousScreenAction();

	List<String> getKeyFieldNames();

	int getMaxRowsCount();

	RowSelectionDefinition getRowSelectionDefinition();

	DrilldownDefinition getDrilldownDefinition();

	boolean isScrollable();

	public interface ColumnDefinition {

		String getName();

		boolean isKey();

		int getStartColumn();

		int getEndColumn();

		boolean isEditable();
	}

	public interface RowSelectionDefinition {

		String getSelectionField();
	}

	@SuppressWarnings("rawtypes")
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
