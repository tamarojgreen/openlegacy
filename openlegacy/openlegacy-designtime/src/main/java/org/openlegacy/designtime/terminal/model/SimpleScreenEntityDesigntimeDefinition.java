package org.openlegacy.designtime.terminal.model;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;

import java.io.Serializable;

public class SimpleScreenEntityDesigntimeDefinition extends SimpleScreenEntityDefinition implements ScreenEntityDesigntimeDefinition, Serializable {

	public SimpleScreenEntityDesigntimeDefinition() {
		super(null, null);
	}

	private static final long serialVersionUID = 1L;

	private TerminalSnapshot terminalSnapshot;

	private String packageName;

	private TerminalRectangle snapshotBorders = null;

	@Override
	public void setEntityName(String entityName) {
		super.setEntityName(entityName);
	}

	@Override
	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	@Override
	public TerminalSnapshot getSnapshot() {
		return terminalSnapshot;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public TerminalRectangle getSnapshotBorders() {
		if (snapshotBorders == null) {
			TerminalPosition topLeft = new SimpleTerminalPosition(1, 1);
			ScreenSize size = getSnapshot().getSize();
			TerminalPosition buttomRight = new SimpleTerminalPosition(size.getRows(), size.getColumns());
			snapshotBorders = new SimpleTerminalRectangle(topLeft, buttomRight);
		}
		return snapshotBorders;
	}

	public void setSnapshotBorders(TerminalRectangle snapshotBorders) {
		this.snapshotBorders = snapshotBorders;
		setWindow(true);
	}
}
