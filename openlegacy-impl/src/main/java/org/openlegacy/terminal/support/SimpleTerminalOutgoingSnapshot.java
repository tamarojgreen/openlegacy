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
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;

import java.util.List;

public class SimpleTerminalOutgoingSnapshot extends AbstractSnapshot implements TerminalOutgoingSnapshot {

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;
	private TerminalSendAction terminalSendAction;

	/*
	 * for serialization purpose only
	 */
	public SimpleTerminalOutgoingSnapshot() {}

	public SimpleTerminalOutgoingSnapshot(TerminalSnapshot terminalSnapshot, TerminalSendAction terminalSendAction) {
		this.terminalSnapshot = terminalSnapshot;
		this.terminalSendAction = terminalSendAction;
	}

	@Override
	public SnapshotType getSnapshotType() {
		return SnapshotType.OUTGOING;
	}

	public TerminalSnapshot getTerminalSnapshot() {
		return terminalSnapshot;
	}

	@Override
	public TerminalSendAction getTerminalSendAction() {
		return terminalSendAction;
	}

	@Override
	protected ScreenSize initScreenSize() {
		return terminalSnapshot.getSize();
	}

	@Override
	public List<TerminalRow> getRows() {
		return terminalSnapshot.getRows();
	}

	@Override
	public List<TerminalField> getFields() {
		return terminalSnapshot.getFields();
	}

	@Override
	protected List<TerminalPosition> initFieldSeperators() {
		return terminalSnapshot.getFieldSeperators();
	}

	@Override
	protected TerminalPosition initCursorPosition() {
		if (terminalSendAction.getCursorPosition() != null) {
			return terminalSendAction.getCursorPosition();
		}
		return terminalSnapshot.getCursorPosition();
	}

	@Override
	public TerminalField getField(TerminalPosition position) {
		return SnapshotUtils.getField(terminalSnapshot, position);
	}

	@Override
	public Object getDelegate() {
		return terminalSnapshot.getDelegate();
	}

	@Override
	protected String initText() {
		return terminalSnapshot.getText();
	}

	@Override
	public String getText(TerminalPosition position, int length) {
		return terminalSnapshot.getText(position, length);
	}

	@Override
	public TerminalRow getRow(int rowNumber) {
		return terminalSnapshot.getRow(rowNumber);
	}

	@Override
	public Integer getSequence() {
		return terminalSnapshot.getSequence()+1;
	}

	@Override
	public String getCommand() {
		return terminalSendAction.getCommand().toString();
	}

	@Override
	protected List<TerminalField> initFields() {
		return terminalSnapshot.getFields();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.support.AbstractSnapshot#readExternal(org.openlegacy.terminal.persistance.TerminalPersistedSnapshot)
	 */
	@Override
	protected void readExternal(TerminalPersistedSnapshot persistedSnapshot) {
		// TODO check is it implementation right
		this.terminalSendAction = persistedSnapshot.getTerminalSendAction();
		this.terminalSnapshot = persistedSnapshot;
	}

}
