package org.openlegacy.terminal;

public interface PositionedPart {

	void setWidth(int width);

	void setPartPosition(TerminalPosition position);

	TerminalPosition getPartPosition();

	int getWidth();
}
