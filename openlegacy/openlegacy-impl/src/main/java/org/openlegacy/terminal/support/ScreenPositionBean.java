package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;

/**
 * A screen position class for bean configuration comfort purposes
 * 
 */
public class ScreenPositionBean implements ScreenPosition {

	private int row;
	private int column;

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public static ScreenPositionBean newInstance(ScreenPosition position) {
		ScreenPositionBean screenPosition = new ScreenPositionBean();
		screenPosition.setRow(position.getRow());
		screenPosition.setColumn(position.getColumn());
		return screenPosition;

	}
}
