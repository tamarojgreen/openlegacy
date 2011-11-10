package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class TerminalConnectionDelegator implements TerminalConnection {

	@Inject
	private ApplicationContext applicationContext;

	private TerminalConnection terminalConnection;

	private TerminalScreen terminalScreen;

	private final static Log logger = LogFactory.getLog(TerminalConnectionDelegator.class);

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
			logger.info("Opened new session");
		}
	}

	public void disconnect() {
		logger.info("Disconnecting session");

		if (terminalConnection == null) {
			logger.debug("Session not connected");
			return;
		}
		TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
		terminalConnectionFactory.disconnect(terminalConnection);
		terminalConnection = null;
		terminalScreen = null;
	}

	public boolean isConnected() {
		return terminalConnection != null;
	}
}
