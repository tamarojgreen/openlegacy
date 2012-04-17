package org.openlegacy.providers.h3270;

import org.h3270.host.Field;
import org.h3270.host.InputField;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;

public class H3270TerminalField extends AbstractTerminalField {

	private Field s3270Field;
	private TerminalPosition position;
	private TerminalPosition endPosition;
	private int lineOffset;
	private int endColumn;

	private String value;

	public H3270TerminalField(Field s3270Field, int lineOffset, int endColumn) {
		this.s3270Field = s3270Field;
		this.lineOffset = lineOffset;
		this.endColumn = endColumn;
	}

	public String getValue() {
		if (value != null) {
			return value;
		}

		if (s3270Field.isMultiline()) {
			value = s3270Field.getValue(lineOffset);
		} else {
			value = s3270Field.getValue();
		}
		if (isEditable()) {
			value = value.replace('_', ' ');
		}
		value = value.replace((char)0, ' ');
		value = StringUtil.rightTrim(value);

		return value;
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
		return H3270Utils.convertForeColor(s3270Field.getExtendedColor());
	}

	public Color getBackColor() {
		return H3270Utils.convertForeColor(s3270Field.getExtendedHighlight());
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

	public boolean isBold() {
		return s3270Field.isIntensified();
	}

	public boolean isReversed() {
		return s3270Field.hasExtendedHighlight();
	}

}
