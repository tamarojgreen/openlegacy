package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.List;

public class SimpleTerminalOutgoingSnapshot extends AbstractSnapshot implements TerminalOutgoingSnapshot {

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;
	private TerminalSendAction terminalSendAction;

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

	public TerminalSendAction getTerminalSendAction() {
		return terminalSendAction;
	}

	public ScreenSize getSize() {
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

	public List<TerminalPosition> getFieldSeperators() {
		return terminalSnapshot.getFieldSeperators();
	}

	public TerminalPosition getCursorPosition() {
		if (terminalSendAction.getCursorPosition() != null) {
			return terminalSendAction.getCursorPosition();
		}
		return terminalSnapshot.getCursorPosition();
	}

	@Override
	public TerminalField getField(TerminalPosition position) {
		return SnapshotUtils.getField(terminalSnapshot, position);
	}

	public Object getDelegate() {
		return terminalSnapshot.getDelegate();
	}

	public String getText() {
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

	public Integer getSequence() {
		return terminalSnapshot.getSequence();
	}

	public String getCommand() {
		return terminalSendAction.getCommand().toString();
	}

	@Override
	protected List<TerminalField> buildAllFields() {
		return terminalSnapshot.getFields();
	}

}
