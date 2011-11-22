package org.openlegacy.terminal;

import org.openlegacy.Snapshot;

import java.util.Collection;
import java.util.List;

/**
 * A terminal screen interface. Defines common terminal screen properties Legacy vendors needs to implement this class
 */
public interface TerminalSnapshot extends Snapshot {

	public enum SnapshotType {
		INCOMING,
		OUTGOING
	}

	ScreenPosition getCursorPosition();

	ScreenSize getSize();

	List<TerminalRow> getRows();

	Collection<TerminalField> getFields();

	SnapshotType getSnapshotType();

	List<ScreenPosition> getFieldSeperators();

	TerminalField getField(ScreenPosition position);

	String getText();

	String getText(ScreenPosition position, int length);

	Object getDelegate();

}
