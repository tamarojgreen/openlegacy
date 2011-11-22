package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class TerminalOutgoingSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;
	private TerminalSendAction terminalSendAction;

	public TerminalOutgoingSnapshot(TerminalSnapshot terminalSnapshot, TerminalSendAction terminalSendAction) {
		this.terminalSnapshot = terminalSnapshot;
		this.terminalSendAction = terminalSendAction;
	}

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

	public List<TerminalRow> getRows() {
		return terminalSnapshot.getRows();
	}

	public Collection<TerminalField> getFields() {
		return terminalSnapshot.getFields();
	}

	public List<ScreenPosition> getFieldSeperators() {
		return terminalSnapshot.getFieldSeperators();
	}

	public ScreenPosition getCursorPosition() {
		if (terminalSendAction.getCursorPosition() != null) {
			return terminalSendAction.getCursorPosition();
		}
		return terminalSnapshot.getCursorPosition();
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

	public TerminalField getField(ScreenPosition position) {
		return ScreenUtils.getField(terminalSnapshot.getRows(), position);
	}

	public Object getDelegate() {
		return terminalSnapshot.getDelegate();
	}

	public String getText() {
		return terminalSnapshot.getText();
	}

	public String getText(ScreenPosition position, int length) {
		return terminalSnapshot.getText(position, length);
	}

}
