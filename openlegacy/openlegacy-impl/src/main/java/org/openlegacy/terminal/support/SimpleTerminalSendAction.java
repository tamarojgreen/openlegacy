package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.ArrayList;
import java.util.List;

public class SimpleTerminalSendAction implements TerminalSendAction {

	private List<TerminalField> modifiedFields = new ArrayList<TerminalField>();
	private Object command;
	private ScreenPosition cursorPosition;

	public SimpleTerminalSendAction(Object command) {
		this.command = command;
	}

	public List<TerminalField> getModifiedFields() {
		return modifiedFields;
	}

	public Object getCommand() {
		return command;
	}

	public ScreenPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(ScreenPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
	}
}
