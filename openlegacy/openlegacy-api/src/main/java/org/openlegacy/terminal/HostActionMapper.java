package org.openlegacy.terminal;

import org.openlegacy.terminal.actions.TerminalAction;

public interface HostActionMapper {

	Object getCommand(Class<? extends TerminalAction> hostAction);
}
