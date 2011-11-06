package org.openlegacy.terminal.definitions;

import java.util.List;

public interface TableDefinition {

	Class<?> getRowClass();

	String getName();

	int getStartRow();

	int getEndRow();

	List<ColumnDefinition> getColumnDefinitions();

	public interface ColumnDefinition {

		String getName();

		boolean isKey();

		int getStartColumn();

		int getEndColumn();
	}

}
