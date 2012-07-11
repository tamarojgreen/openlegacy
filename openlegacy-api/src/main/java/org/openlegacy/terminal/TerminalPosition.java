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

	public int getColumn();

	public TerminalPosition next();

	TerminalPosition previous();

	public TerminalPosition moveBy(int columns);
}
