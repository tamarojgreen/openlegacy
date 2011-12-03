package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.runtime.field.GXIField;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

public class ApxTerminalField extends AbstractTerminalField {

	private final GXIField apxField;
	private TerminalPosition position;
	private String modifiedValue;

	public ApxTerminalField(GXIField field) {
		this.apxField = field;
	}

	public String getValue() {
		if (modifiedValue != null) {
			return modifiedValue;
		}
		return apxField.getContent();
	}

	public boolean isEditable() {
		return !apxField.isProtected();
	}

	public TerminalPosition getPosition() {
		if (position == null) {
			position = SimpleTerminalPosition.newInstance(apxField.getPosition().getRow(), apxField.getPosition().getColumn());
		}
		return position;
	}

	public int getLength() {
		return apxField.getLength();
	}

	public void setValue(String value) {
		modifiedValue = value;
	}

	public boolean isModified() {
		return modifiedValue != null;
	}
}
