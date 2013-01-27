/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal;

import java.io.Serializable;

/**
 * A simple definition for terminal position model with row & column properties
 * 
 */
public interface TerminalPosition extends Comparable<TerminalPosition>, Serializable {

	int getRow();

	int getColumn();

	/**
	 * Create a new terminal position in the next position on the terminal snapshot
	 * 
	 * @return the next terminal position
	 */
	TerminalPosition next();

	/**
	 * Create a new terminal position in the previous position on the terminal snapshot
	 * 
	 * @return the previous terminal position
	 */
	TerminalPosition previous();

	/**
	 * Creates a new position with offset by the given columns. Limited to {@link ScreenSize.DEFAULT_COLUMNS}. Use
	 * <code>SnapshotUtil.moveBy</code> if screen size is known from a snapshot in the method context.
	 * 
	 * @param columns
	 * @return
	 */
	TerminalPosition moveBy(int columns);

	int getAbsolutePosition(ScreenSize screenSize);
}
