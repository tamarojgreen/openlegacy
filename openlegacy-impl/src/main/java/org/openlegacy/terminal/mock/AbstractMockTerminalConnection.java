package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.TerminalConnection;

public abstract class AbstractMockTerminalConnection implements TerminalConnection {

	public String getSessionId() {
		return "mock session";
	}

}
