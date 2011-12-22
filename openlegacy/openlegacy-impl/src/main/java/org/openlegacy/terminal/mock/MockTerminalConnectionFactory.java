package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.TerminalConnection;

public class MockTerminalConnectionFactory extends AbstractMockTerminalConnectionFactory {

	public TerminalConnection getConnection() {

		return new MockTerminalConnection(fetchSnapshots());
	}

}
