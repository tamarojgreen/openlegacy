package org.openlegacy.modules.table.drilldown;

/**
 * A table row comparator interface. Check whether the given row POJO matches the given row keys
 * 
 */
public interface RowComparator<T> {

	boolean isRowMatch(T tableRow, Object... rowKeys);
}
