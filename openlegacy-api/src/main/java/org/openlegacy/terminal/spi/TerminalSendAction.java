package org.openlegacy.terminal.spi;

import org.openlegacy.SendAction;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public interface TerminalSendAction extends SendAction {

	List<TerminalField> getModifiedFields();

	Object getCommand();

	TerminalPosition getCursorPosition();

	void setCursorPosition(TerminalPosition fieldPosition);
}