package org.openlegacy.terminal.definitions;

import java.util.ArrayList;
import java.util.List;

public class SimpleTableDefinition implements TableDefinition {

	private String name;
	private Class<?> rowClass;

	private int startRow;
	private int endRow;
	private List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();

	public SimpleTableDefinition(Class<?> rowClass, String name) {
		this.rowClass = rowClass;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Class<?> getRowClass() {
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

}
