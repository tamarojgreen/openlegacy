package org.openlegacy.terminal;

/**
 * A screen position class for bean configuration comfort purposes
 * 
 */
public class ScreenPositionBean extends ScreenPosition {

	public ScreenPositionBean() {
		super(0, 0);
	}

	@Override
	public void setRow(int row) {
		super.setRow(row);
	}

	@Override
	public void setColumn(int column) {
		super.setColumn(column);
	}

}
