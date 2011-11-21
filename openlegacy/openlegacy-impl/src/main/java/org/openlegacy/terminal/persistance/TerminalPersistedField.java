package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.support.ScreenPositionBean;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedField implements TerminalField {

	@XmlElement(name = "position", type = ScreenPositionBean.class)
	private ScreenPosition screenPosition;

	@XmlAttribute
	private String value;
	@XmlAttribute
	private int length;

	@XmlAttribute
	private boolean modified;

	@XmlAttribute
	private boolean editable;

	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}

	public void setScreenPosition(ScreenPosition screenPosition) {
		this.screenPosition = screenPosition;
	}

	public ScreenPosition getPosition() {
		return screenPosition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
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

}
