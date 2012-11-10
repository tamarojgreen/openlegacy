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
package org.openlegacy.terminal.persistance;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.ModifiableTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;
import org.openlegacy.utils.StringUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedField implements ModifiableTerminalField {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int column;

	@XmlTransient
	private int row;

	@XmlTransient
	private TerminalPosition position;

	@XmlTransient
	private TerminalPosition endPosition;

	@XmlAttribute
	private String value = "";

	@XmlAttribute
	private Integer length;

	/**
	 * NOTE! - All Boolean fields should have default value set. XML serializer checks if the default value change, and reset it
	 * to reduce XML size
	 */

	@XmlAttribute
	private Boolean modified = false;

	@XmlAttribute
	private Boolean editable = false;

	@XmlAttribute
	private Boolean hidden = false;

	@XmlAttribute
	private Color color = Color.GREEN;

	@XmlAttribute
	private Color backColor = Color.BLACK;

	@XmlAttribute
	private Class<?> type = String.class;

	@XmlAttribute
	private Boolean bold = false;

	@XmlAttribute
	private String visualValue;

	@XmlAttribute
	private Boolean rightToLeft = false;

	public TerminalPosition getPosition() {
		if (position == null) {
			position = SimpleTerminalPosition.newInstance(row, column);
		}
		return position;
	}

	public void setPosition(TerminalPosition position) {
		row = position.getRow();
		column = position.getColumn();
		// reset position
		this.position = null;
	}

	public void setRow(int row) {
		this.row = row;
		// reset position
		position = null;
	}

	public String getValue() {
		// do not allow null based terminal fields. empty string if null (might be null due to XML de-serialization)
		if (value == null) {
			value = "";
		}
		return value;
	}

	public void setValue(String value, boolean modified) {
		this.modified = modified;
		this.value = value;

	}

	public void setValue(String value) {
		setValue(value, true);
	}

	public int getLength() {
		if (length == null) {
			return getValue().length();
		}
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void resetLength() {
		length = null;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public TerminalPosition getEndPosition() {
		if (endPosition == null) {
			endPosition = SnapshotUtils.getEndPosition(this);
		}
		return endPosition;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isEmpty() {
		return StringUtil.isEmpty(getValue());
	}

	public boolean isPassword() {
		return isEditable() && isHidden();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalField)) {
			return false;
		}
		TerminalField otherField = (TerminalField)obj;
		return TerminalEqualsHashcodeUtil.fieldEquals(this, otherField);
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.fieldHashCode(this);
	}

	@Override
	public String toString() {
		return SnapshotUtils.fieldToString(this);
	}

	public Class<?> getType() {

		if (type != String.class) {
			return type;
		}
		return StringUtil.getTypeByValue(getValue());
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(Boolean bold) {
		this.bold = bold;
	}

	public boolean isReversed() {
		return getBackColor() != null && getBackColor() != Color.BLACK;
	}

	@Override
	public TerminalField clone() {
		try {
			return (TerminalField)super.clone();
		} catch (CloneNotSupportedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public void setEndPosition(TerminalPosition endPosition) {
		this.endPosition = endPosition;
	}

	public String getVisualValue() {
		return visualValue;
	}

	public void setVisualValue(String visualValue) {
		this.visualValue = visualValue;
	}

	public String getOriginalValue() {
		return getValue();
	}

	public Object getDelegate() {
		return null;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}
}
