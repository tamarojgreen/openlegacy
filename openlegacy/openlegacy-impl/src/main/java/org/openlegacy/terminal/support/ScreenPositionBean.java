package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.ScreenPosition;

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
public class ScreenPositionBean implements ScreenPosition {

	@XmlAttribute
	private int row = 1;

	@XmlAttribute
	private int column = 1;

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

	public int compareTo(ScreenPosition o) {
		return SnapshotUtils.comparePositions(this, o);
	}
}
