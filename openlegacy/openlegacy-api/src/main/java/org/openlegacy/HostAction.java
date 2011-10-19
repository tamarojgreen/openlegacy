package org.openlegacy;


/**
 * A simple interface for defining host action. Depending on the implementation,
 * TerminalSession vendors needs implement the command executing.
 * For example "pf1", "enter", etc.
 *
 */
public interface HostAction {

	Object getCommand();
}
