package org.openlegacy.terminal;

/**
 * Terminal session factory class. Legacy vendors needs to implement this class
 * 
 */
public interface TerminalConnectionFactory {

	TerminalConnection getConnection();
}
