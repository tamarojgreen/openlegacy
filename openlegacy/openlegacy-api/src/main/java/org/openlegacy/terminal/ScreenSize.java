package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public interface ScreenSize extends Serializable {

	public int getRows();

	public int getColumns();

}
