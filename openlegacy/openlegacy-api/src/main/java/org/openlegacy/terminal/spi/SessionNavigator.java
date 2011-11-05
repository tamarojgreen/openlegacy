package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

public interface SessionNavigator {

	void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws ScreenEntityNotAccessibleException;
}
