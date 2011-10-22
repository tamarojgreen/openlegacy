package org.openlegacy.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.spi.TerminalSendAction;

/**
 * Emulation vendors needs to implement this class
 */
public interface TerminalConnection {

	TerminalScreen getSnapshot();

	TerminalConnection doAction(HostAction hostAction);

	TerminalConnection doAction(TerminalSendAction terminalSendAction);

	Object getDelegate();
}
