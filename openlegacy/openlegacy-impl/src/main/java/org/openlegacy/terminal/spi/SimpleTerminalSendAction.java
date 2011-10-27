package org.openlegacy.terminal.spi;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.ScreenPosition;

import java.util.Map;

public class SimpleTerminalSendAction implements TerminalSendAction {

	private Map<ScreenPosition, String> fields;
	private HostAction hostAction;
	private ScreenPosition cursorPosition;

	public SimpleTerminalSendAction(Map<ScreenPosition, String> fields, HostAction hostAction, ScreenPosition cursorPosition) {
		this.fields = fields;
		this.hostAction = hostAction;
		this.cursorPosition = cursorPosition;
	}

	public Map<ScreenPosition, String> getFields() {
		return fields;
	}

	public HostAction getHostAction() {
		return hostAction;
	}

	public ScreenPosition getCursorPosition() {
		return cursorPosition;
	}
}
