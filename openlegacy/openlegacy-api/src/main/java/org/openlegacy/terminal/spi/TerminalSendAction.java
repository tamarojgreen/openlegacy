package org.openlegacy.terminal.spi;

import org.openlegacy.SendAction;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public interface TerminalSendAction extends SendAction {

	List<TerminalField> getModifiedFields();

	Object getCommand();

	ScreenPosition getCursorPosition();

	void setCursorPosition(ScreenPosition fieldPosition);
}