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
package org.openlegacy.terminal.mock;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.exceptions.TerminalActionException;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.ModifiedFieldsCriteria;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockSendValidationUtils {

	public static void validateSendAction(TerminalSnapshot expectedOutgoingSnapshot, TerminalSendAction terminalSendAction)
			throws TerminalActionException {
		List<TerminalField> expectedModifiedFields = FieldsQuery.queryFields(expectedOutgoingSnapshot,
				ModifiedFieldsCriteria.instance());
		List<TerminalField> actualModifiedFields = terminalSendAction.getFields();

		validateFieldsMatch(expectedModifiedFields, actualModifiedFields);

		TerminalPosition actualCursorPosition = terminalSendAction.getCursorPosition();
		TerminalPosition expectedCursorPosition = expectedOutgoingSnapshot.getCursorPosition();
		validateCursorMatch(actualCursorPosition, expectedCursorPosition);

	}

	private static void validateCursorMatch(TerminalPosition actualCursorPosition, TerminalPosition expectedCursorPosition) {
		String message = MessageFormat.format("Expected cursor is not matched. Expected {0}, actual:{1}", expectedCursorPosition,
				actualCursorPosition);

		if (actualCursorPosition != null && expectedCursorPosition != null) {
			boolean cursorMatch = actualCursorPosition.equals(expectedCursorPosition);
			if (!cursorMatch) {
				throw (new TerminalActionException(message));
			}
		}
	}

	public static void validateFieldsMatch(List<TerminalField> expectedFieldsList, List<TerminalField> actualFieldsList)
			throws TerminalActionException {
		if (expectedFieldsList.size() != actualFieldsList.size()) {
			throw (new TerminalActionException(MessageFormat.format("Fields list dont match the expected sent fields: {0}",
					expectedFieldsList)));
		}
		Map<TerminalPosition, TerminalField> actualFieldsMap = toMap(actualFieldsList);
		for (TerminalField exptectedField : expectedFieldsList) {
			TerminalPosition expectedPosition = exptectedField.getPosition();
			TerminalField actualField = actualFieldsMap.get(expectedPosition);
			if (actualField == null) {
				throw (new TerminalActionException(MessageFormat.format(
						"Expected field in position {0} not found in the actual sent fields", expectedPosition)));
			}
			if (!StringUtils.equals(exptectedField.getValue(), actualField.getValue())) {
				throw (new TerminalActionException(MessageFormat.format(
						"Expected value ''{0}'' doesnt match sent value ''{1}'' for field in position {2}",
						exptectedField.getValue(), actualField.getValue(), expectedPosition)));
			}
		}
	}

	public static Map<TerminalPosition, TerminalField> toMap(List<TerminalField> fieldsList) {
		Map<TerminalPosition, TerminalField> fieldsMap = new HashMap<TerminalPosition, TerminalField>();
		for (TerminalField terminalField : fieldsList) {
			fieldsMap.put(terminalField.getPosition(), terminalField);
		}
		return fieldsMap;

	}
}
