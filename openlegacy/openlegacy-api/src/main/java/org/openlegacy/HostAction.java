package org.openlegacy;

/**
 * A simple interface for defining host action. Depending on the implementation, TerminalSession vendors needs implement the
 * command executing. For example "f1", "enter", etc.
 * 
 */
public interface HostAction<S extends HostSession> {

	void perform(S session, Object entity);
}
