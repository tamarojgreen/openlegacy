package org.openlegacy.terminal;

/**
 * Defines a field on a terminal screen
 * 
 */
public interface TerminalField extends TerminalPositionContainer {

	TerminalPosition getPosition();

	TerminalPosition getEndPosition();

	String getValue();

	void setValue(String value);

	int getLength();

	boolean isEditable();

	boolean isModified();

}
