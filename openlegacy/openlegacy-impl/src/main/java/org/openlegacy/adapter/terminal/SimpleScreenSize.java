package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.ScreenSize;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public class SimpleScreenSize implements ScreenSize {

	private int rows;
	private int columns;

	public SimpleScreenSize(int rows, int columns) {}

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
