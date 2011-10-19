package org.openlegacy.terminal;

import java.text.MessageFormat;

/**
 * A simple definition for screen position model with row & column properties 
 *
 */
public class ScreenPosition {

	private int row;
	private int column;

	public ScreenPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public static ScreenPosition newInstance(int row,int column){
		return new ScreenPosition(row,column);
	}

	protected void setRow(int row) {
		this.row = row;
	}
	protected void setColumn(int column) {
		this.column = column;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", row, column);
	}
}
