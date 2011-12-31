package org.openlegacy.terminal;

import org.openlegacy.terminal.spi.TerminalSendAction;

public interface TerminalSendActionBuilder<S> {

	TerminalSendAction buildSendAction(TerminalSnapshot terminalSnapshot, S source);
}
