package org.openlegacy.terminal;

import org.openlegacy.terminal.spi.TerminalSendAction;

public interface TerminalOutgoingSnapshot extends TerminalSnapshot {

	TerminalSendAction getTerminalSendAction();
}
