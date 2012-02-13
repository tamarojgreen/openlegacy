package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class TerminalConnectionDelegator implements TerminalConnection {

	@Inject
	private ApplicationContext applicationContext;

	private TerminalConnection terminalConnection;

	private TerminalSnapshot terminalSnapshot;

	private final static Log logger = LogFactory.getLog(TerminalConnectionDelegator.class);

	public TerminalSnapshot getSnapshot() {
		lazyConnect();
		if (terminalSnapshot == null) {
			terminalSnapshot = terminalConnection.fetchSnapshot();
		}
		return terminalSnapshot;
	}

	public TerminalConnection doAction(TerminalSendAction terminalSendAction) {
		lazyConnect();
		terminalSnapshot = null;
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
		terminalSnapshot = null;
	}

	public boolean isConnected() {
		return terminalConnection != null;
	}

	public TerminalSnapshot fetchSnapshot() {
		terminalSnapshot = null;
		return getSnapshot();
	}

	public String getSessionId() {
		if (terminalConnection == null) {
			return null;
		}
		return terminalConnection.getSessionId();
	}
}
