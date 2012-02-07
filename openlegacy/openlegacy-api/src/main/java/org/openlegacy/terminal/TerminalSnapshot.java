package org.openlegacy.terminal;

import org.openlegacy.Snapshot;

import java.io.Serializable;
import java.util.List;

/**
 * A terminal snapshot interface. Defines common terminal snapshot properties Legacy vendors needs to implement this class
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
	 * especially in testing mode. Use getSize().getRows and getRow to iterate on all rows
	 */
	List<TerminalRow> getRows();

	TerminalRow getRow(int rowNumber);

	List<TerminalField> getFields();

	SnapshotType getSnapshotType();

	List<TerminalPosition> getFieldSeperators();

	TerminalField getField(TerminalPosition position);

	String getText();

	String getText(TerminalPosition position, int length);

	Object getDelegate();

	Integer getSequence();

	String getCommand();
}
