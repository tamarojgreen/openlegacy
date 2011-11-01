package org.openlegacy.terminal.spi;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.terminal.TerminalSession;

public interface SessionNavigator {

	void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws HostEntityNotAccessibleException;
}
