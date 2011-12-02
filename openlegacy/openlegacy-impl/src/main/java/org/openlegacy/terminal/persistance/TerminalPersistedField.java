package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.support.ScreenUtils;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedField implements TerminalField {

	@XmlAttribute
	private int column;

	@XmlTransient
	private int row;

	@XmlTransient
	private ScreenPosition screenPosition;

	@XmlAttribute
	private String value;

	@XmlAttribute
	private Integer length;

	@XmlAttribute
	private Boolean modified = false;

	@XmlAttribute
	private Boolean editable = false;

	public ScreenPosition getPosition() {
		if (screenPosition == null) {
			screenPosition = SimpleScreenPosition.newInstance(row, column);
		}
		return screenPosition;
	}

	public void setPosition(ScreenPosition position) {
		row = position.getRow();
		column = position.getColumn();
		// reset position
		screenPosition = null;
	}

	public void setRow(int row) {
		this.row = row;
		// reset position
		screenPosition = null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
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
		return ScreenUtils.fieldToString(this);
	}
}
