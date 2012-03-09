package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;

import java.util.List;

public interface TableDefinition<C extends ColumnDefinition> {

	Class<?> getTableClass();

	String getTableEntityName();

	int getEndRow();

	List<C> getColumnDefinitions();

	C getColumnDefinition(String fieldName);

	List<String> getKeyFieldNames();

	String getMainDisplayField();

	int getMaxRowsCount();

	boolean isScrollable();

	public interface ColumnDefinition extends Comparable<ColumnDefinition> {

		String getName();

		boolean isKey();

		boolean isEditable();

		String getDisplayName();

		String getSampleValue();

		Class<?> getJavaType();
	}

}
