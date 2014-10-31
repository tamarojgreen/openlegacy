package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

/**
 * Represents @Identifier annotation
 * 
 * @author Ivan Bort
 * 
 */
public class IdentifierModel extends SimpleScreenIdentifier {

	private static final long serialVersionUID = 1879521960174968408L;

	private int row;
	private int column;
	private String text;

	private FieldAttributeType defaultAttribute = FieldAttributeType.Value;

	/**
	 * @param position
	 * @param text
	 */
	public IdentifierModel(TerminalPosition position, String text, FieldAttributeType attribute) {
		super(position, text, false, attribute);
		this.row = position.getRow();
		this.column = position.getColumn();
		this.text = text;
	}

	/**
	 * 
	 */
	public IdentifierModel(int row, int column, String text, FieldAttributeType attribute) {
		super(new SimpleTerminalPosition(row, column), text, false, attribute);
		this.row = row;
		this.column = column;
		this.text = text;
		this.setAttribute(attribute);
	}

	public IdentifierModel(int row, int column, String text) {
		super(new SimpleTerminalPosition(row, column), text, false, null);
		this.row = row;
		this.column = column;
		this.text = text;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	public void setAttributeDefaultValue() {
		setAttribute(FieldAttributeType.Value);
	}

	public FieldAttributeType getDefaultAttribute() {
		return defaultAttribute;
	}
}
