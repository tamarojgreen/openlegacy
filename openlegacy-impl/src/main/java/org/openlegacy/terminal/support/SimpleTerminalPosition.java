/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;

import java.text.MessageFormat;

/**
 * A simple definition for position model with row & column properties
 * 
 */
public class SimpleTerminalPosition implements TerminalPosition {

	private static final long serialVersionUID = 1L;

	private int row = 1;
	private int column = 1;

	public SimpleTerminalPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public static SimpleTerminalPosition newInstance(int row, int column) {
		return new SimpleTerminalPosition(row, column);
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
		if (!(obj instanceof TerminalPosition)) {
			return false;
		}
		TerminalPosition otherPosition = (TerminalPosition)obj;
		boolean equals = new EqualsBuilder().append(getRow(), otherPosition.getRow()).append(getColumn(),
				otherPosition.getColumn()).isEquals();
		return equals;
	}

	public int compareTo(TerminalPosition o) {
		return SnapshotUtils.comparePositions(this, o);
	}

	public TerminalPosition next() {
		return moveBy(1);
	}

	public TerminalPosition previous() {
		return moveBy(-1);
	}

	public TerminalPosition moveBy(int columns) {
		if (columns == 0) {
			return this;
		}
		return SimpleTerminalPosition.newInstance(row, column + columns);
	}

	public int getAbsolutePosition(ScreenSize screenSize) {
		return SnapshotUtils.toAbsolutePosition(this, screenSize);
	}
}
