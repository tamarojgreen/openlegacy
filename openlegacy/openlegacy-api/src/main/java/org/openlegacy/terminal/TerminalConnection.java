package org.openlegacy.terminal;

import org.openlegacy.terminal.spi.TerminalSendAction;

/**
 * Emulation providers needs to implement this class
 */
public interface TerminalConnection {

	TerminalSnapshot getSnapshot();

	TerminalSnapshot fetchSnapshot();

	TerminalConnection doAction(TerminalSendAction terminalSendAction);

	Object getDelegate();

	String getSessionId();

}
