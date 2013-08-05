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
package org.openlegacy.ide.eclipse.preview.screen;

import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.text.MessageFormat;

/**
 * Contains position information of field in terminal and field value.
 * 
 * @author Ivan Bort
 * 
 */
public class FieldRectangle {

	private int row;
	private int endRow;
	private int column;
	private int endColumn;
	private String value;

	/**
	 * 
	 */
	public FieldRectangle(int row, int endRow, int column, int endColumn, String value) {
		this.row = row;
		this.endRow = endRow;
		this.column = column;
		this.endColumn = endColumn;
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getColumn() {
		return column;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public String getValue() {
		return value;
	}

	public String toCoordsString() {
		return MessageFormat.format(Messages.getString("field.rectangle.string"), this.row, this.column, this.endRow,
				this.endColumn);
	}

	public TerminalPosition getStartPosition() {
		return new SimpleTerminalPosition(row, column);
	}
}
