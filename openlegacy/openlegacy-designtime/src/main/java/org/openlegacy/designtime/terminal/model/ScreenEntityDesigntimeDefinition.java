package org.openlegacy.designtime.terminal.model;

import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public interface ScreenEntityDesigntimeDefinition extends ScreenEntityDefinition {

	public abstract void setEntityName(String entityName);

	public abstract void setDisplayName(String displayName);

	TerminalSnapshot getSnapshot();

	public abstract void setSnapshot(TerminalSnapshot terminalSnapshot);

	String getPackageName();

	public abstract void setPackageName(String packageName);

	TerminalRectangle getSnapshotBorders();

	void setSnapshotBorders(TerminalRectangle snapshotBorders);
}