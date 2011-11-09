package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class SimpleTerminalSendAction implements TerminalSendAction {

	private List<TerminalField> modifiedFields;
	private Object command;
	private ScreenPosition cursorPosition;

	public SimpleTerminalSendAction(List<TerminalField> modifiedFields, Object command, ScreenPosition cursorPosition) {
		this.modifiedFields = modifiedFields;
		this.command = command;
		this.cursorPosition = cursorPosition;
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
