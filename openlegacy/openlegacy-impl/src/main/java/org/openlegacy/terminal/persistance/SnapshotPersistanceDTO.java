package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.TerminalOutgoingSnapshot;
import org.openlegacy.utils.ReflectionUtil;

import java.util.List;

public class SnapshotPersistanceDTO {

	public static TerminalPersistedSnapshot transformSnapshot(TerminalSnapshot snapshot) {
		if (snapshot instanceof TerminalOutgoingSnapshot) {
			return transformOutgoingSnapshot((TerminalOutgoingSnapshot)snapshot);
		}
		return transformIncomingSnapshot(snapshot);

	}

	public static TerminalPersistedSnapshot transformIncomingSnapshot(TerminalSnapshot snapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		ReflectionUtil.copyProperties(persistedSnapshot, snapshot);

		transformCommonSnapshot(persistedSnapshot, snapshot);

		return persistedSnapshot;
	}

	private static TerminalPersistedSnapshot transformOutgoingSnapshot(TerminalOutgoingSnapshot snapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		persistedSnapshot.setSnapshotType(snapshot.getSnapshotType());

		TerminalSnapshot terminalSnapshot = snapshot.getTerminalSnapshot();

		transformCommonSnapshot(persistedSnapshot, terminalSnapshot);

		TerminalSendAction sendAction = snapshot.getTerminalSendAction();
		List<TerminalField> fields = sendAction.getModifiedFields();
		for (TerminalField terminalField : fields) {
			ScreenPosition fieldPosition = terminalField.getPosition();
			TerminalPersistedRow row = (TerminalPersistedRow)persistedSnapshot.getRow(fieldPosition.getRow());
			TerminalPersistedField field = (TerminalPersistedField)row.getField(fieldPosition.getColumn());
			field.setValue(terminalField.getValue());
			field.setModified(true);
		}
		return persistedSnapshot;
	}

	private static TerminalSnapshot transformCommonSnapshot(TerminalPersistedSnapshot persistedSnapshot, TerminalSnapshot snapshot) {

		ReflectionUtil.copyProperties(persistedSnapshot, snapshot);

		List<TerminalRow> rows = snapshot.getRows();
		for (TerminalRow terminalRow : rows) {
			TerminalPersistedRow persistedRow = new TerminalPersistedRow();
			ReflectionUtil.copyProperties(persistedRow, terminalRow);

			List<TerminalField> fields = terminalRow.getFields();
			for (TerminalField terminalField : fields) {
				TerminalPersistedField persistedField = new TerminalPersistedField();
				ReflectionUtil.copyProperties(persistedField, terminalField);
				// avoid persistence of length attribute if it's the same size as the value length
				if (persistedField.getValue().length() == persistedField.getLength()) {
					persistedField.resetLength();
				}
				persistedRow.getFields().add(persistedField);
			}
			if (persistedRow.getFields().size() > 0) {
				persistedSnapshot.getRows().add(persistedRow);
			}
		}
		return persistedSnapshot;
	}
}
