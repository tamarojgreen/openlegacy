package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.ScreenPosition;

import java.text.MessageFormat;

/**
 * A simple definition for screen position model with row & column properties
 * 
 */
public class SimpleScreenPosition implements ScreenPosition {

	private int row = 1;
	private int column = 1;

	public SimpleScreenPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public static SimpleScreenPosition newInstance(int row, int column) {
		return new SimpleScreenPosition(row, column);
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getRow()).append(getColumn()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenPosition)) {
			return false;
		}
		ScreenPosition otherPosition = (ScreenPosition)obj;
		return new EqualsBuilder().append(getRow(), otherPosition.getRow()).append(getColumn(), otherPosition.getColumn()).isEquals();
	}
}
