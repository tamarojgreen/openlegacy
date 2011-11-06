package org.openlegacy.applinx;

import com.sabratec.applinx.common.runtime.field.GXIField;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.support.SimpleScreenPosition;

public class ApxTerminalField implements TerminalField {

	private final GXIField apxField;
	private ScreenPosition position;
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

	public ScreenPosition getPosition() {
		if (position == null) {
			position = SimpleScreenPosition.newInstance(apxField.getPosition().getRow(), apxField.getPosition().getColumn());
		}
		return position;
	}

	public int getLength() {
		return apxField.getLength();
	}

	public void setValue(String value) {
		modifiedValue = value;
	}

}
