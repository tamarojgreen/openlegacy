package org.openlegacy.terminal;

import org.openlegacy.terminal.actions.TerminalAction;

public interface TerminalActionMapper {

	Object getCommand(Class<? extends TerminalAction> terminalAction);
}
