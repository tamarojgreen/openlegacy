package org.openlegacy.adapter.terminal.trail;

import org.openlegacy.adapter.terminal.ScreenPositionBean;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;

import javax.xml.bind.annotation.XmlElement;

public class TerminalTrailField implements TerminalField {

	@XmlElement(name = "position")
	private ScreenPositionBean screenPosition;
	private String value;
	private int length;

	private boolean modified;

	private boolean editable;

	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}

	public void setScreenPosition(ScreenPositionBean screenPosition) {
		this.screenPosition = screenPosition;
	}

	public ScreenPositionBean getPosition() {
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
}
