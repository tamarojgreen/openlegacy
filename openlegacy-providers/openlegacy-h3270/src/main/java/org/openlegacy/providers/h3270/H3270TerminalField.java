package org.openlegacy.providers.h3270;

import org.h3270.host.Field;
import org.h3270.host.InputField;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

public class H3270TerminalField extends AbstractTerminalField {

	private Field s3270Field;
	private TerminalPosition position;
	private TerminalPosition endPosition;
	private int lineOffset;
	private int endColumn;

	public H3270TerminalField(Field s3270Field, int lineOffset, int endColumn) {
		this.s3270Field = s3270Field;
		this.lineOffset = lineOffset;
		this.endColumn = endColumn;
	}

	public String getValue() {
		if (s3270Field.isMultiline()) {
			return s3270Field.getValue(lineOffset);
		}
		return s3270Field.getValue();
	}

	public void setFocus() {
		if (s3270Field instanceof InputField) {
			((InputField)s3270Field).setFocused(true);
		}
	}

	public TerminalPosition getPosition() {
		if (position == null) {
			if (lineOffset > 0) {
				position = new SimpleTerminalPosition(s3270Field.getStartY() + lineOffset + 1, 1);
			} else {
				position = new SimpleTerminalPosition(s3270Field.getStartY() + lineOffset + 1, s3270Field.getStartX() + 1);
			}
		}
		return position;
	}

	public TerminalPosition getEndPosition() {
		if (endPosition == null) {
			endPosition = new SimpleTerminalPosition(s3270Field.getEndY() + 1, endColumn);
		}
		return endPosition;
	}

	public int getLength() {
		return endColumn - s3270Field.getStartX() + 1;
	}

	public boolean isEditable() {
		return s3270Field instanceof InputField;
	}

	public boolean isHidden() {
		return s3270Field.isHidden();
	}

	public Color getColor() {
		return Color.GREEN;
	}

	public Color getBackColor() {
		return Color.BLACK;
	}

	public Class<?> getType() {
		if (s3270Field instanceof InputField) {
			InputField inputField = (InputField)s3270Field;
			if (inputField.isNumeric()) {
				return Double.class;
			}
		}
		return String.class;
	}

}
