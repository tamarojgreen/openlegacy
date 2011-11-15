package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;

import java.util.List;

import javax.inject.Inject;

/**
 * Default implementation of row finder. Scans the list of rows and ask row comparator whether a given row is a match
 * 
 */
public class DefaultRowFinder implements RowFinder {

	@Inject
	private RowComparator rowComparator;

	public Integer findRow(List<?> tableRows, Object... rowKeys) {
		int rowCount = 0;

		for (Object tableRow : tableRows) {
			if (rowComparator.isRowMatch(tableRow, rowKeys)) {
				return rowCount;
			}
			rowCount++;
		}
		return null;
	}

	public void setRowComparator(RowComparator rowComparator) {
		this.rowComparator = rowComparator;
	}
}
