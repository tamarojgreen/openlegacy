package org.openlegacy.terminal;

/**
 * Defines a field on a terminal screen
 * 
 */
public interface TerminalField {

	ScreenPosition getPosition();

	String getValue();

	int getLength();

	boolean isEditable();

}
