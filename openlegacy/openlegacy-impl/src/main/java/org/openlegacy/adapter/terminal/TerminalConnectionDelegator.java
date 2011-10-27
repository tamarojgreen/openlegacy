package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class TerminalConnectionDelegator implements TerminalConnection, InitializingBean {

	@Autowired
	private ApplicationContext applicationContext;

	private TerminalConnection terminalConnection;

	private TerminalScreen terminalScreen;

	public TerminalScreen getSnapshot() {
		if (terminalScreen == null) {
			terminalScreen = terminalConnection.getSnapshot();
		}
		return terminalScreen;
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		terminalScreen = null;
		return terminalConnection.doAction(terminalSendAction);
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

	public void afterPropertiesSet() throws Exception {
		TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
		terminalConnection = terminalConnectionFactory.getConnection();

	}

}
