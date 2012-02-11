package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleTerminalOutgoingSnapshot;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.StringUtil;

import java.util.Iterator;
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
			field.setValue(terminalField.getValue());
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
			boolean modified = terminalField.isModified();
			terminalField.setValue(StringUtil.rightTrim(terminalField.getValue()));
			if (!modified) {
				persistedField.setModified(false);
			}
		}
	}

	private static void collectRowFields(List<TerminalPosition> fieldSeperators, TerminalRow terminalRow,
			TerminalPersistedRow persistedRow) {
		List<TerminalField> fields = terminalRow.getFields();
		for (Iterator<TerminalField> iterator = fields.iterator(); iterator.hasNext();) {
			TerminalField field = iterator.next();

			TerminalPersistedField persistedField = new TerminalPersistedField();
			ReflectionUtil.copyProperties(persistedField, field);
			persistedField.setModified(false);
			// avoid persistence of length attribute if it's the same size as the value length
			if (persistedField.getValue().length() == persistedField.getLength()) {
				persistedField.resetLength();
			}
			// gather all read-only fields which has not separator between them
			// when persisting a snapshot, the persisted snapshot should not split read-only field unless defined that way by
			// the host
			while (!fieldSeperators.contains(field.getEndPosition().next())) {
				if (!iterator.hasNext()) {
					break;
				}
				if (field.isEditable()) {
					break;
				}
				if (field.getPosition().getRow() != persistedField.getPosition().getRow()) {
					break;
				}

				field = iterator.next();
				persistedField.setValue(persistedField.getValue() + field.getValue());
			}
			persistedRow.getFields().add(persistedField);
		}
	}

}
