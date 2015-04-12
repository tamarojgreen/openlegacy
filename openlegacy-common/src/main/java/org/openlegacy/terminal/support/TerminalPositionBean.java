/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A screen position class for bean configuration comfort purposes
 * 
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPositionBean implements TerminalPosition {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int row = 1;

	@XmlAttribute
	private int column = 1;

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public static TerminalPositionBean newInstance(TerminalPosition position) {
		TerminalPositionBean newPosition = new TerminalPositionBean();
		newPosition.setRow(position.getRow());
		newPosition.setColumn(position.getColumn());
		return newPosition;

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
		return new EqualsBuilder().append(getRow(), otherPosition.getRow()).append(getColumn(), otherPosition.getColumn()).isEquals();
	}

	@Override
	public int compareTo(TerminalPosition o) {
		return SnapshotUtils.comparePositions(this, o, false);
	}

	@Override
	public TerminalPosition next() {
		return moveBy(1);
	}

	@Override
	public TerminalPosition previous() {
		return moveBy(-1);
	}

	@Override
	public TerminalPosition moveBy(int columns) {
		if (columns == 0) {
			return this;
		}
		int offsetRows = columns / ScreenSize.DEFAULT_COLUMN;
		columns = columns % ScreenSize.DEFAULT_COLUMN;
		return SimpleTerminalPosition.newInstance(row + offsetRows, column + columns);
	}

	@Override
	public int getAbsolutePosition(ScreenSize screenSize) {
		return SnapshotUtils.toAbsolutePosition(this, screenSize);
	}
}
