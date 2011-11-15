package org.openlegacy.terminal.definitions;

import org.openlegacy.HostAction;

import java.util.List;

public interface TableDefinition {

	Class<?> getTableClass();

	int getStartRow();

	int getEndRow();

	List<ColumnDefinition> getColumnDefinitions();

	HostAction getNextScreenAction();

	HostAction getPreviousScreenAction();

	List<String> getKeyFieldNames();

	int getMaxRowsCount();

	RowSelectionDefinition getRowSelectionDefinition();

	public interface ColumnDefinition {

		String getName();

		boolean isKey();

		int getStartColumn();

		int getEndColumn();
	}

	public interface RowSelectionDefinition {

		String getSelectionField();

	}
}
