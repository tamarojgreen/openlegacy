package org.openlegacy.terminal.utils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.Arrays;

public class TerminalEqualsHashcodeUtil {

	public static boolean snapshotsEquals(TerminalSnapshot snapshot1, TerminalSnapshot snapshot2) {
		return snapshot1.getRows().containsAll(snapshot2.getRows());
	}

	public static int snapshotHashcode(TerminalSnapshot terminalSnapshot) {
		return terminalSnapshot.getRows().toArray().hashCode();
	}

	public static boolean rowEquals(TerminalRow row1, TerminalRow row2) {
		return Arrays.equals(row1.getFields().toArray(), row2.getFields().toArray());
	}

	public static int rowHashCode(TerminalRow row) {
		return row.getFields().toArray().hashCode();
	}

	public static boolean fieldEquals(TerminalField field1, TerminalField field2) {
		return new EqualsBuilder().append(field1.getPosition(), field2.getPosition()).append(field1.getLength(),
				field2.getLength()).append(field1.isEditable(), field2.isEditable()).append(field1.getValue(), field2.getValue()).isEquals();
	}

	public static int fieldHashCode(TerminalField field) {
		return new HashCodeBuilder().append(field.getPosition()).append(field.isEditable()).append(field.getValue()).hashCode();
	}
}
