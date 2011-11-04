package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.ScreenPosition;

import java.util.Map;

public interface TerminalSendAction {

	Map<ScreenPosition, String> getFields();

	Object getCommand();

	ScreenPosition getCursorPosition();
}