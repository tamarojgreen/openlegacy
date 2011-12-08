package org.openlegacy.designtime.terminal.model;

import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;

public interface ScreenEntityDesigntimeDefinition extends ScreenEntityDefinition {

	public abstract void setEntityName(String entityName);

	public abstract void setDisplayName(String displayName);

	TerminalSnapshot getSnapshot();

	public abstract void setSnapshot(TerminalSnapshot terminalSnapshot);

	String getPackageName();

	public abstract void setPackageName(String packageName);

	TerminalRectangle getSnapshotBorders();

	void setSnapshotBorders(TerminalRectangle snapshotBorders);

	/**
	 * Adds a table to the screen definition without a name yet. Name will be given by the screen entity name at a pater phase
	 */
	void addTemporaryTable(SimpleTableDefinition tableDefinition);
}