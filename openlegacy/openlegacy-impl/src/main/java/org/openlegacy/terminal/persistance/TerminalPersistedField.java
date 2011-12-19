package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedField extends AbstractTerminalField {

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

}
