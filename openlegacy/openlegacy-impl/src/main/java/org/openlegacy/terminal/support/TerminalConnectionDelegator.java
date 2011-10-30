package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TerminalConnectionDelegator implements TerminalConnection {

	@Autowired
	private ApplicationContext applicationContext;

	private TerminalConnection terminalConnection;

	private TerminalScreen terminalScreen;

	public TerminalScreen getSnapshot() {
		lazyConnect();
		if (terminalScreen == null) {
			terminalScreen = terminalConnection.getSnapshot();
		}
		return terminalScreen;
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		lazyConnect();
		terminalScreen = null;
		return terminalConnection.doAction(terminalSendAction);
	}

	public Object getDelegate() {
		lazyConnect();
		return terminalConnection.getDelegate();
	}

	private void lazyConnect() {
		if (terminalConnection == null) {
			TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
			terminalConnection = terminalConnectionFactory.getConnection();
		}
	}

}
