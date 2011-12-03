package org.openlegacy.terminal;

/**
 * A simple definition for terminal position model with row & column properties
 * 
 */
public interface TerminalPosition extends Comparable<TerminalPosition> {

	int getRow();

	public int getColumn();
}
