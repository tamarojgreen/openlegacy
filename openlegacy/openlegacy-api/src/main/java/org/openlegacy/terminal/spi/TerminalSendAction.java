package org.openlegacy.terminal.spi;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.ScreenPosition;

import java.util.Map;

public interface TerminalSendAction {

	Map<ScreenPosition, String> getFields();

	HostAction getHostAction();

	ScreenPosition getCursorPosition();
}