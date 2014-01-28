/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;

import java.text.MessageFormat;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public class SimpleScreenSize implements ScreenSize {

	private static final long serialVersionUID = 1L;

	private int rows = ScreenSize.DEFAULT_ROWS;
	private int columns = ScreenSize.DEFAULT_COLUMN;

	public static SimpleScreenSize DEFAULT = new SimpleScreenSize(ScreenSize.DEFAULT_ROWS, ScreenSize.DEFAULT_COLUMN);

	public SimpleScreenSize() {}

	public SimpleScreenSize(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", rows, columns);
	}

	public boolean contains(TerminalPosition terminalPosition) {
		return SnapshotUtils.contains(this, terminalPosition);
	}

}
