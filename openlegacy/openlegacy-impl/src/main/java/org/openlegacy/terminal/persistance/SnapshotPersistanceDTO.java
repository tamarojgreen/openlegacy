package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.ScreenPositionBean;
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
			TerminalPersistedRow row = (TerminalPersistedRow)persistedSnapshot.getRows().get(fieldPosition.getRow() - 1);
			TerminalPersistedField field = (TerminalPersistedField)row.getField(fieldPosition.getColumn());
			field.setValue(terminalField.getValue());
			field.setModified(true);
		}
		return persistedSnapshot;
	}

	private static TerminalSnapshot transformCommonSnapshot(TerminalPersistedSnapshot persistedSnapshot, TerminalSnapshot screen) {

		List<TerminalRow> rows = screen.getRows();
		persistedSnapshot.setCursorPosition(screen.getCursorPosition());
		for (TerminalRow terminalRow : rows) {
			TerminalPersistedRow persistedRow = new TerminalPersistedRow();
			ReflectionUtil.copyProperties(persistedRow, terminalRow);

			List<TerminalField> fields = terminalRow.getFields();
			for (TerminalField terminalField : fields) {
				TerminalPersistedField persistedField = new TerminalPersistedField();
				ReflectionUtil.copyProperties(persistedField, terminalField);
				persistedField.setScreenPosition(ScreenPositionBean.newInstance(terminalField.getPosition()));
				persistedRow.getFields().add(persistedField);
			}
			persistedSnapshot.getRows().add(persistedRow);
		}
		return persistedSnapshot;
	}
}
