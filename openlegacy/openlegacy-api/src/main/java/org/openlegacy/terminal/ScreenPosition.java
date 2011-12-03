package org.openlegacy.terminal;

/**
 * A simple definition for screen position model with row & column properties
 * 
 */
public interface ScreenPosition extends Comparable<ScreenPosition> {

	int getRow();

	public int getColumn();
}
