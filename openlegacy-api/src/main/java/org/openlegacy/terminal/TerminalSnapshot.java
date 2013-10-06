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

import org.openlegacy.Snapshot;

import java.io.Serializable;
import java.util.List;

/**
 * A terminal snapshot interface. Defines common terminal snapshot properties Legacy vendors needs to implement this class.
 * Designed to be implemented by emulation providers. A snapshot can be of type INCOMING or OUTGOING.
 * 
 * @see TerminalOutgoingSnapshot
 */
public interface TerminalSnapshot extends Snapshot, Serializable {

	public enum SnapshotType {
		INCOMING,
		OUTGOING
	}

	TerminalPosition getCursorPosition();

	ScreenSize getSize();

	/**
	 * Return the terminal snapshot rows. NOTE: Rows are 0 base, and it is not enforced that all terminal snapshots are filled,
	 * especially in testing mode. Use <code>getSize().getRows()</code> and <code>getRow(int rowNumber)</code> to iterate on all
	 * rows
	 */
	List<TerminalRow> getRows();

	/**
	 * Returns the specified row number on the screen
	 * 
	 * @param rowNumber
	 *            the requested row number
	 * @return terminal row
	 */
	TerminalRow getRow(int rowNumber);

	/**
	 * Designed to return fields based on the attributes separation.
	 * 
	 * @return
	 */
	List<TerminalField> getFields();

	SnapshotType getSnapshotType();

	List<TerminalPosition> getFieldSeperators();

	TerminalField getField(TerminalPosition position);

	TerminalField getField(int row, int column);

	String getText();

	String getText(TerminalPosition position, int length);

	String getLogicalText(TerminalPosition position, int length);

	/**
	 * Get access to the underlying implementation
	 * 
	 * @return the underlying implementation
	 */
	Object getDelegate();

	Integer getSequence();

	String getCommand();

	boolean isRightToLeft();
}
