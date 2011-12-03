package org.openlegacy.terminal;

public interface RowPart {

	TerminalPosition getPosition();

	String getValue();

	int getLength();

	boolean isEditable();
}
