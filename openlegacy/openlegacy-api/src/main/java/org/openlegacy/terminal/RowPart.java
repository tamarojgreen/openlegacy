package org.openlegacy.terminal;

public interface RowPart {

	ScreenPosition getPosition();

	String getValue();

	int getLength();

	boolean isEditable();
}
