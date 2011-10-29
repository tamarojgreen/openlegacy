package org.openlegacy.adapter.terminal;

import org.openlegacy.adapter.HostSessionModuleAdapter;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSessionModule;
import org.openlegacy.terminal.spi.TerminalSendAction;

/**
 * Define a terminal session override-able methods which happens before & after a terminal session action
 * 
 */
public class TerminalSessionModuleAdapter extends HostSessionModuleAdapter implements TerminalSessionModule {

	public void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		// allow override
	}

	public void afterSendAction(TerminalConnection terminalConnection) {
		// allow override

	}

}
