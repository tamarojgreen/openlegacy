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

	TerminalRow getRow(int rowNumber);

	/**
	 * Designed to return fields based on the attributes separation.
	 * 
	 * @return
	 */
	List<TerminalField> getFields();

	/**
	 * Designed to return fields based on the logical separation. For example split by 2 or more blanks, colors, etc.
	 * 
	 */
	List<TerminalField> getLogicalFields();

	SnapshotType getSnapshotType();

	List<TerminalPosition> getFieldSeperators();

	TerminalField getField(TerminalPosition position);

	String getText();

	String getText(TerminalPosition position, int length);

	Object getDelegate();

	Integer getSequence();

	String getCommand();
}
