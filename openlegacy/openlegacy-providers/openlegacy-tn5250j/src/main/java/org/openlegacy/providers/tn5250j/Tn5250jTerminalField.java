package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractTerminalField;
import org.openlegacy.terminal.support.SnapshotUtils;

public class Tn5250jTerminalField extends AbstractTerminalField {

	private static final long serialVersionUID = 1L;

	private TerminalPosition position;

	private String value;
	private int length;

	private boolean hidden = false;
	private Color color;
	private Color backColor;

	private boolean underline;

	private int fieldAttributes;

	public Tn5250jTerminalField(String value, TerminalPosition position, int length, int fieldAttributes) {
		this.value = value;
		this.position = position;
		this.length = length;
		this.fieldAttributes = fieldAttributes;
		Tn5250jUtils.applyAttributeToField(this, fieldAttributes);
	}

	@Override
	public TerminalPosition initPosition() {
		return position;
	}

	@Override
	public TerminalPosition initEndPosition() {
		return SnapshotUtils.getEndPosition(this);
	}

	@Override
	public String initValue() {
		return value;
	}

	@Override
	public int initLength() {
		return length;
	}

	public boolean isEditable() {
		return false;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public Class<?> getType() {
		return String.class;
	}

	public boolean isBold() {
		// TODO implement bold for 5250j
		return false;
	}

	public boolean isReversed() {
		return getColor() != Color.BLACK;
	}

	@Override
	public TerminalField clone() {
		Tn5250jTerminalField field = new Tn5250jTerminalField(value, position, length, fieldAttributes);
		return field;
	}
}
