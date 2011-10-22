package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalSession;

/**
 * Define an interface for terminal vendors to implement on how to send fields to the terminalSession
 * 
 */
public interface ScreenEntitySender {

	void perform(TerminalSession terminalSession, TerminalSendAction terminalSendAction);

}
