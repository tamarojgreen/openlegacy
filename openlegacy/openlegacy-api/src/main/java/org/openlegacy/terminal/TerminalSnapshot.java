package org.openlegacy.terminal;

import org.openlegacy.Snapshot;

import java.util.List;

/**
 * A terminal screen interface. Defines common terminal screen properties Legacy vendors needs to implement this class
 */
public interface TerminalSnapshot extends Snapshot {

	public enum SnapshotType {
		INCOMING,
		OUTGOING
	}

	ScreenSize getSize();

	List<TerminalRow> getRows();

	SnapshotType getSnapshotType();

	List<ScreenPosition> getAttributes();
}
