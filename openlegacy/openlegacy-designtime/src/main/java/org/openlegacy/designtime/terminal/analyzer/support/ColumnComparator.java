package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.modules.table.TableColumnFact;

import java.util.Comparator;

public class ColumnComparator implements Comparator<TableColumnFact> {

	private static ColumnComparator instance = new ColumnComparator();

	public static ColumnComparator instance() {
		return instance;
	}

	public int compare(TableColumnFact o1, TableColumnFact o2) {
		return o1.getFields().get(0).getPosition().getColumn() - o2.getFields().get(0).getPosition().getColumn();
	}

}
