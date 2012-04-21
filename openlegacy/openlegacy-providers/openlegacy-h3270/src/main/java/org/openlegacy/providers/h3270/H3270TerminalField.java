package org.openlegacy.providers.h3270;

import org.h3270.host.Field;
import org.h3270.host.InputField;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;

public class H3270TerminalField extends AbstractTerminalField {

	private Field s3270Field;
	private int lineOffset;
	private int startColumn;
	private int endColumn;

	public H3270TerminalField(Field s3270Field, int lineOffset, int startColumn, int endColumn) {
		this.s3270Field = s3270Field;
		this.lineOffset = lineOffset;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	@Override
	public String initValue() {

		String value;
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

	@Override
	public TerminalPosition initPosition() {
		TerminalPosition position;
		if (lineOffset > 0) {
			position = new SimpleTerminalPosition(s3270Field.getStartY() + lineOffset + 1, 1);
		} else {
			position = new SimpleTerminalPosition(s3270Field.getStartY() + lineOffset + 1, s3270Field.getStartX() + 1);
		}
		return position;
	}

	@Override
	public TerminalPosition initEndPosition() {
		return new SimpleTerminalPosition(s3270Field.getEndY() + 1, endColumn);
	}

	@Override
	public int initLength() {
		return endColumn - startColumn + 1;
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
		return H3270Utils.convertBackColor(s3270Field.getExtendedHighlight());
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

	@Override
	public TerminalField clone() {
		H3270TerminalField field = new H3270TerminalField(s3270Field, lineOffset, startColumn, endColumn);
		return field;

	}
}
