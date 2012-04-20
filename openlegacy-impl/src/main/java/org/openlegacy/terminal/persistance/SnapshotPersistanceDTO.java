package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleTerminalOutgoingSnapshot;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;

import java.util.List;

public class SnapshotPersistanceDTO {

	public static TerminalPersistedSnapshot transformSnapshot(TerminalSnapshot snapshot) {
		if (snapshot instanceof SimpleTerminalOutgoingSnapshot) {
			return transformOutgoingSnapshot((SimpleTerminalOutgoingSnapshot)snapshot);
		}
		return transformIncomingSnapshot(snapshot);

	}

	public static TerminalPersistedSnapshot transformIncomingSnapshot(TerminalSnapshot snapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		ReflectionUtil.copyProperties(persistedSnapshot, snapshot);

		transformCommonSnapshot(persistedSnapshot, snapshot);

		return persistedSnapshot;
	}

	private static TerminalPersistedSnapshot transformOutgoingSnapshot(SimpleTerminalOutgoingSnapshot snapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		TerminalSnapshot terminalSnapshot = snapshot.getTerminalSnapshot();

		transformCommonSnapshot(persistedSnapshot, terminalSnapshot);

		persistedSnapshot.setSnapshotType(snapshot.getSnapshotType());

		TerminalSendAction sendAction = snapshot.getTerminalSendAction();

		persistedSnapshot.setCommand(sendAction.getCommand().toString());

		List<TerminalField> fields = sendAction.getModifiedFields();
		for (TerminalField terminalField : fields) {
			TerminalPosition fieldPosition = terminalField.getPosition();
			TerminalPersistedRow row = (TerminalPersistedRow)persistedSnapshot.getRow(fieldPosition.getRow());
			TerminalPersistedField field = (TerminalPersistedField)row.getField(fieldPosition.getColumn());
			field.setValue(terminalField.getValue(), false);
			field.setModified(true);
		}
		return persistedSnapshot;
	}

	private static TerminalSnapshot transformCommonSnapshot(TerminalPersistedSnapshot persistedSnapshot, TerminalSnapshot snapshot) {

		ReflectionUtil.copyProperties(persistedSnapshot, snapshot);

		List<TerminalRow> rows = snapshot.getRows();
		List<TerminalPosition> fieldSeperators = snapshot.getFieldSeperators();

		for (TerminalRow terminalRow : rows) {
			TerminalPersistedRow persistedRow = new TerminalPersistedRow();
			ReflectionUtil.copyProperties(persistedRow, terminalRow);

			collectRowFields(fieldSeperators, terminalRow, persistedRow);

			// don't copy empty rows
			if (persistedRow.getFields().size() == 0) {
				continue;
			}
			// don't copy row with single empty field
			if (persistedRow.getFields().size() == 1 && StringUtil.isEmpty(persistedRow.getFields().get(0).getValue())) {
				continue;
			}

			formatFieldsForCleanXml(persistedRow);
			persistedSnapshot.getRows().add(persistedRow);
		}
		return persistedSnapshot;
	}

	private static void formatFieldsForCleanXml(TerminalPersistedRow persistedRow) {
		List<TerminalField> fields = persistedRow.getFields();
		for (TerminalField terminalField : fields) {
			TerminalPersistedField persistedField = (TerminalPersistedField)terminalField;
			// String value = StringUtil.rightTrim(terminalField.getValue());
			String value = terminalField.getValue().replace((char)0, ' ');
			persistedField.setValue(value, false);
			// disable default colors from saved XML
			if (persistedField.getColor() == Color.BLACK || persistedField.getColor() == Color.LIGHT_GREEN) {
				persistedField.setColor(null);
			}
		}
	}

	private static void collectRowFields(List<TerminalPosition> fieldSeperators, TerminalRow terminalRow,
			TerminalPersistedRow persistedRow) {
		List<TerminalField> fields = terminalRow.getFields();
		for (TerminalField field : fields) {
			TerminalPersistedField persistedField = new TerminalPersistedField();
			ReflectionUtil.copyProperties(persistedField, field);
			persistedField.setModified(false);
			// avoid persistence of length attribute if it's the same size as the value length
			if (persistedField.getValue().length() == persistedField.getLength()) {
				persistedField.resetLength();
			}
			persistedRow.getFields().add(persistedField);
		}
	}

}
