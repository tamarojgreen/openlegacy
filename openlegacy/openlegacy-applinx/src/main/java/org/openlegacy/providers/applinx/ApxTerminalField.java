package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.runtime.field.GXBlockModeCommonFieldData;
import com.sabratec.applinx.common.runtime.field.GXIField;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

public class ApxTerminalField extends AbstractTerminalField {

	private static final long serialVersionUID = 1L;

	private final GXIField apxField;
	private TerminalPosition position;
	private String modifiedValue;

	private TerminalPosition endPosition;

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

	public TerminalPosition getEndPosition() {
		if (endPosition == null) {
			endPosition = SnapshotUtils.getEndPosition(this);
		}
		return endPosition;
	}

	public boolean isHidden() {
		GXBlockModeCommonFieldData commonData = (GXBlockModeCommonFieldData)apxField.getCommonData();
		return commonData.isHidden();
	}

	public Color getColor() {
		return Color.GREEN;
		// TODO APX colors;
		// return apxField.getCommonData().getForegroundColor();
	}

	public Color getBackColor() {
		return Color.BLACK;
		// TODO APX colors;
		// return apxField.getCommonData().getBackgroundColor();
	}
}
