package org.openlegacy.terminal.definitions;

import org.openlegacy.HostAction;

import java.util.ArrayList;
import java.util.List;

public class SimpleTableDefinition implements TableDefinition {

	private Class<?> rowClass;

	private int startRow;
	private int endRow;
	private List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();

	private HostAction nextScreenAction;
	private HostAction previousScreenAction;

	private SimpleRowSelectionDefinition rowSelectionDefinition = new SimpleRowSelectionDefinition();

	public SimpleTableDefinition(Class<?> rowClass) {
		this.rowClass = rowClass;
	}

	public Class<?> getTableClass() {
		return rowClass;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	public HostAction getNextScreenAction() {
		return nextScreenAction;
	}

	public void setNextScreenAction(HostAction nextScreenAction) {
		this.nextScreenAction = nextScreenAction;
	}

	public HostAction getPreviousScreenAction() {
		return previousScreenAction;
	}

	public void setPreviousScreenAction(HostAction previousScreenAction) {
		this.previousScreenAction = previousScreenAction;
	}

	public List<String> getKeyFieldNames() {
		List<String> keyFields = new ArrayList<String>();
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.isKey()) {
				keyFields.add(columnDefinition.getName());
			}
		}
		return keyFields;
	}

	public int getMaxRowsCount() {
		// TODO handle (future) gaps
		return getEndRow() - getStartRow() + 1;
	}

	public SimpleRowSelectionDefinition getRowSelectionDefinition() {
		return rowSelectionDefinition;
	}

	public static class SimpleRowSelectionDefinition implements RowSelectionDefinition {

		private String selectionField;

		public String getSelectionField() {
			return selectionField;
		}

		public void setSelectionField(String selectionField) {
			this.selectionField = selectionField;
		}
	}

	public ColumnDefinition getColumnDefinition(String fieldName) {
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.getName().equals(fieldName)) {
				return columnDefinition;
			}
		}
		return null;
	}
}
