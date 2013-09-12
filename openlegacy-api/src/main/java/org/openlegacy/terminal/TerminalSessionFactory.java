package org.openlegacy.terminal;

public interface TerminalSessionFactory {

	TerminalSession getSession();

	void returnSession(TerminalSession session);
}
