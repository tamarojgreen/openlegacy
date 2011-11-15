package org.openlegacy.modules.table.drilldown;

/**
 * A table row comparator interface. Check whether the given row POJO matches the given row keys
 * 
 */
public interface RowComparator {

	boolean isRowMatch(Object tableRow, Object... rowKeys);
}
