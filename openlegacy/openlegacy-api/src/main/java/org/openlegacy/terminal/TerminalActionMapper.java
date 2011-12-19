package org.openlegacy.terminal;

import org.openlegacy.terminal.actions.TerminalAction;

public interface TerminalActionMapper {

	Object getCommand(TerminalAction terminalAction);

	TerminalAction getAction(Object command);
}
