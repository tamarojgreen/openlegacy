package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;

public class SimpleColumnDefinition implements ColumnDefinition {

	private String name;
	private int startColumn;
	private int endColumn;

	public SimpleColumnDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

}
