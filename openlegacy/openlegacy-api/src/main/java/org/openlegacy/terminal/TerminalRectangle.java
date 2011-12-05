package org.openlegacy.terminal;

public interface TerminalRectangle {

	TerminalPosition getTopLeftPosition();

	TerminalPosition getButtomRightPosition();

	boolean contains(TerminalPosition position, boolean includeBorder);
}
