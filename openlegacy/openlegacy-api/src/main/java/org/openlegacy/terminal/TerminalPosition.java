package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * A simple definition for terminal position model with row & column properties
 * 
 */
public interface TerminalPosition extends Comparable<TerminalPosition>, Serializable {

	int getRow();

	public int getColumn();

	public TerminalPosition next();
}
