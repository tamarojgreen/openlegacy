package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
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
public class TerminalPersistedField implements TerminalField {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int column;

	@XmlTransient
	private int row;

	@XmlTransient
	private TerminalPosition position;

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
		return value;
	}

	public void setValue(String value) {
		this.modified = true;
		this.value = value;
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
		return SnapshotUtils.getEndPosition(this);
	}

	public boolean isHidden() {
		return hidden;
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

	public Color getBackColor() {
		return backColor;
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
}
