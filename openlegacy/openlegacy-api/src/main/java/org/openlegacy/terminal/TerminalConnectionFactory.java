package org.openlegacy.terminal;

/**
 * Terminal session factory class. Emulation providers needs to implement this class
 * 
 */
public interface TerminalConnectionFactory {

	TerminalConnection getConnection();
}
