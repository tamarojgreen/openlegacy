package org.openlegacy.terminal.spi;

import java.util.Map;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalSession;

/**
 * Define an interface for terminal vendors to implement on how to send fields to the terminalSession  
 *
 */
public interface ScreenEntitySender {

	void perform(TerminalSession terminalSession,
			Map<ScreenPosition,String> fields,
			HostAction hostAction, ScreenPosition cursorPosition);

}
