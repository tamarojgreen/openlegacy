package org.openlegacy.terminal.support;

import org.openlegacy.support.HostSessionModuleAdapter;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionListener;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionModule;
import org.openlegacy.terminal.spi.TerminalSendAction;

import javax.inject.Inject;

/**
 * Define a terminal session override-able methods which happens before & after a terminal session action
 * 
 */
public class TerminalSessionModuleAdapter extends HostSessionModuleAdapter implements TerminalSessionModule, TerminalConnectionListener {

	@Inject
	private TerminalSession terminalSession;

	public void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		// allow override
	}

	public void afterSendAction(TerminalConnection terminalConnection) {
		// allow override

	}

	public TerminalSession getTerminalSession() {
		return terminalSession;
	}
}
