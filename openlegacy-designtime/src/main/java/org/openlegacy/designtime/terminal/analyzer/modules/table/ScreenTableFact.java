package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;

import java.util.List;

public class ScreenTableFact implements ScreenFact {

	private List<TableColumnFact> tableColumns;

	public ScreenTableFact(List<TableColumnFact> tableColumns) {
		this.tableColumns = tableColumns;
	}

	public List<TableColumnFact> getTableColumns() {
		return tableColumns;
	}
}
