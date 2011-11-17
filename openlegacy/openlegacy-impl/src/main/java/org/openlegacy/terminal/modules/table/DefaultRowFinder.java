package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;

import java.util.List;

/**
 * Default implementation of row finder. Scans the list of rows and ask row comparator whether a given row is a match
 * 
 */
public class DefaultRowFinder<T> implements RowFinder<T> {

	public Integer findRow(RowComparator<T> rowComparator, List<T> tableRows, Object... rowKeys) {
		int rowCount = 0;

		for (T tableRow : tableRows) {
			if (rowComparator.isRowMatch(tableRow, rowKeys)) {
				return rowCount;
			}
			rowCount++;
		}
		return null;
	}
}
