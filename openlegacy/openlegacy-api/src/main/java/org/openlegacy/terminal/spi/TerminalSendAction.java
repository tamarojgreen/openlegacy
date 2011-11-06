package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public interface TerminalSendAction {

	List<TerminalField> getModifiedFields();

	Object getCommand();

	ScreenPosition getCursorPosition();
}