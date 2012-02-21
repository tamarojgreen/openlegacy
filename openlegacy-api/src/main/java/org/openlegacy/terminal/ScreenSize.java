package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public interface ScreenSize extends Serializable {

	public static final int DEFAULT_ROWS = 24;
	public static final int DEFAULT_COLUMN = 80;

	public int getRows();

	public int getColumns();

}
