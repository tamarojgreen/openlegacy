package org.openlegacy.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.StatefullHostSession;

/**
 * The main entry point for the terminal session. In addition to it's parent classes methods for retrieving the current screen and
 * Legacy vendors needs to implement this class
 */
public interface TerminalSession extends StatefullHostSession<TerminalScreen> {

	TerminalSession doAction(HostAction action, Object screenEntityInstance);

	Object getDelegate();
}
