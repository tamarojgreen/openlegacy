package org.openlegacy.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.StatefullHostSession;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullHostSession<TerminalScreen> {

	/**
	 * 
	 * @param action
	 * @param screenEntity
	 * @return The current screen entity
	 */
	<S extends ScreenEntity> Object doAction(HostAction action, S screenEntity);

	<S extends ScreenEntity, T extends ScreenEntity> T doAction(HostAction action, S screenEntity, Class<T> expectedScreenEntity);

	<S extends ScreenEntity> S getEntity();
}
