package org.openlegacy.applinx;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;

import com.sabratec.applinx.common.runtime.field.GXIField;

public class ApxTerminalField implements TerminalField {

	private final GXIField apxField;
	private ScreenPosition position;

	public ApxTerminalField(GXIField field) {
		this.apxField = field;
	}

	public String getValue() {
		return apxField.getContent();
	}

	public boolean isEditable() {
		return !apxField.isProtected();
	}

	public ScreenPosition getPosition() {
		if (position == null) {
			position = ScreenPosition.newInstance(apxField.getPosition()
					.getRow(), apxField.getPosition().getColumn());
		}
		return position;
	}

	public int getLength() {
		return apxField.getLength();
	}

}
