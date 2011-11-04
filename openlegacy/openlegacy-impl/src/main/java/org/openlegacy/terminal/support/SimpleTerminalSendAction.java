package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.Map;

public class SimpleTerminalSendAction implements TerminalSendAction {

	private Map<ScreenPosition, String> fields;
	private Object command;
	private ScreenPosition cursorPosition;

	public SimpleTerminalSendAction(Map<ScreenPosition, String> fields, Object command, ScreenPosition cursorPosition) {
		this.fields = fields;
		this.command = command;
		this.cursorPosition = cursorPosition;
	}

	public Map<ScreenPosition, String> getFields() {
		return fields;
	}

	public Object getCommand() {
		return command;
	}

	public ScreenPosition getCursorPosition() {
		return cursorPosition;
	}
}
