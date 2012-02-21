package org.openlegacy.modules.table.drilldown;

import java.util.List;

/**
 * Find a row within the given list which matches the given row keys Implementation may use RowComparator to determine if a given
 * row matches the given keys values
 * 
 */
public interface RowFinder<T> {

	Integer findRow(RowComparator<T> rowComparator, List<T> tableRows, Object... rowKeys);

}
