package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

/**
 * Session navigator enables navigating from the current one screen entity to the specified one
 * 
 * @author RoiM
 * 
 */
public interface SessionNavigator {

	void navigate(TerminalSession terminalSession, Class<?> targetScreenEntity) throws ScreenEntityNotAccessibleException;
}
