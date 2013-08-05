/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
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

	public static TerminalPersistedSnapshot transformIncomingSnapshot(TerminalSnapshot incomingSnapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		ReflectionUtil.copyProperties(persistedSnapshot, incomingSnapshot);

		transformCommonSnapshot(persistedSnapshot, incomingSnapshot);

		// When working with mock-up incoming snapshots, it gets "dirty" by changes on the outgoing snapshot - this code reset it
		// in the output cloned incoming snapshot
		if (incomingSnapshot instanceof TerminalPersistedSnapshot) {
			List<TerminalField> sourceFields = incomingSnapshot.getFields();
			for (TerminalField sourceField : sourceFields) {
				if (sourceField.isModified()) {
					TerminalPersistedField persistedField = (TerminalPersistedField)persistedSnapshot.getField(sourceField.getPosition());
					persistedField.setValue(sourceField.getOriginalValue(), false);
				}
			}
		}

		return persistedSnapshot;
	}

	private static TerminalPersistedSnapshot transformOutgoingSnapshot(SimpleTerminalOutgoingSnapshot snapshot) {
		TerminalPersistedSnapshot persistedSnapshot = new TerminalPersistedSnapshot();
		TerminalSnapshot terminalSnapshot = snapshot.getTerminalSnapshot();

		transformCommonSnapshot(persistedSnapshot, terminalSnapshot);

		persistedSnapshot.setSnapshotType(snapshot.getSnapshotType());

		TerminalSendAction sendAction = snapshot.getTerminalSendAction();

		persistedSnapshot.setCommand(sendAction.getCommand().toString());

		List<TerminalField> fields = sendAction.getFields();
		for (TerminalField terminalField : fields) {
			TerminalPosition fieldPosition = terminalField.getPosition();
			TerminalPersistedRow row = (TerminalPersistedRow)persistedSnapshot.getRow(fieldPosition.getRow());
			TerminalPersistedField field = (TerminalPersistedField)row.getField(fieldPosition.getColumn());
			String value = terminalField.getValue();
			if (field == null) {
				continue;
			}
			if (field.isPassword()) {
				value = convertToAsteriks(terminalField.getValue());
			}
			field.setValue(value, false);
			field.setModified(true);
		}
		return persistedSnapshot;
	}

	private static String convertToAsteriks(String value) {
		StringBuilder sb = new StringBuilder(value.length());
		for (int i = 0; i < value.length(); i++) {
			sb.append("*");
		}
		return sb.toString();
	}

	private static TerminalSnapshot transformCommonSnapshot(TerminalPersistedSnapshot persistedSnapshot, TerminalSnapshot snapshot) {

		ReflectionUtil.copyProperties(persistedSnapshot, snapshot);

		List<TerminalRow> rows = snapshot.getRows();
		List<TerminalPosition> fieldSeperators = snapshot.getFieldSeperators();

		for (TerminalRow terminalRow : rows) {
			TerminalPersistedRow persistedRow = new TerminalPersistedRow();
			ReflectionUtil.copyProperties(persistedRow, terminalRow);

			collectRowFields(fieldSeperators, terminalRow, persistedRow,
					persistedSnapshot.getSnapshotType() == SnapshotType.INCOMING);

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
			TerminalPersistedRow persistedRow, boolean isIncoming) {
		List<TerminalField> fields = terminalRow.getFields();
		for (TerminalField field : fields) {
			TerminalPersistedField persistedField = new TerminalPersistedField();
			ReflectionUtil.copyProperties(persistedField, field);
			persistedField.setModified(false);
			if (isIncoming) {
				persistedField.setValue(field.getOriginalValue());
			}
			persistedRow.getFields().add(persistedField);
		}
	}

}
