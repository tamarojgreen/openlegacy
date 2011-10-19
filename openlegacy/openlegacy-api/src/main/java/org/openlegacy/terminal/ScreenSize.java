package org.openlegacy.terminal;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public class ScreenSize {

	private int rows;
	private int columns;

	public ScreenSize(int rows, int columns) {}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

}
