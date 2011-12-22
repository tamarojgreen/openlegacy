package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * Defines a field on a terminal screen
 * 
 */
public interface TerminalField extends TerminalPositionContainer, Serializable {

	TerminalPosition getPosition();

	TerminalPosition getEndPosition();

	String getValue();

	void setValue(String value);

	int getLength();

	boolean isEditable();

	boolean isModified();

	boolean isEmpty();

	boolean isHidden();

	boolean isPassword();
}
