package org.openlegacy.terminal;

import org.openlegacy.StatefullSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.spi.TerminalSendAction;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullSession<TerminalSnapshot> {

	<R extends ScreenEntity> R doAction(TerminalAction action);

	/**
	 * 
	 * @param action
	 * @param screenEntity
	 * @return The current screen entity
	 */
	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity);

	<S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity,
			Class<R> expectedScreenEntity);

	void doAction(TerminalSendAction terminalSendAction);

	<S extends ScreenEntity> S getEntity();

	String getSessionId();

}
