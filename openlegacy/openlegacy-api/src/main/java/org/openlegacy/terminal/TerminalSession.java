package org.openlegacy.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.StatefullHostSession;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullHostSession<TerminalScreen> {

	<R extends ScreenEntity> R doAction(HostAction action);

	/**
	 * 
	 * @param action
	 * @param screenEntity
	 * @return The current screen entity
	 */
	<S extends ScreenEntity, R extends ScreenEntity> R doAction(HostAction action, S screenEntity);

	<S extends ScreenEntity, R extends ScreenEntity> R doAction(HostAction action, S screenEntity, Class<R> expectedScreenEntity);

	<S extends ScreenEntity> S getEntity();
}
