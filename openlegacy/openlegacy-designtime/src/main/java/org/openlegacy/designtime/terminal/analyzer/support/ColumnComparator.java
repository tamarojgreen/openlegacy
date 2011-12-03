package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.model.TableColumn;

import java.util.Comparator;

public class ColumnComparator implements Comparator<TableColumn> {

	private static ColumnComparator instance = new ColumnComparator();

	public static ColumnComparator instance() {
		return instance;
	}

	public int compare(TableColumn o1, TableColumn o2) {
		return o1.getFields().get(0).getPosition().getColumn() - o2.getFields().get(0).getPosition().getColumn();
	}

}
