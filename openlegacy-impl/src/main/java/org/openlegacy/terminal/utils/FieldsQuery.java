/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.utils;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FieldsQuery {

	public static List<TerminalField> queryFields(TerminalSnapshot snapshot, FieldsCriteria criteria) {
		List<TerminalRow> rows = snapshot.getRows();
		return queryFields(rows, criteria);
	}

	public static List<TerminalField> queryFields(List<TerminalRow> rows, FieldsCriteria criteria) {
		List<TerminalField> matchedFields = new ArrayList<TerminalField>();

		for (TerminalRow terminalRow : rows) {
			List<TerminalField> fields = terminalRow.getFields();
			for (TerminalField field : fields) {
				if (criteria.match(field)) {
					matchedFields.add(field);
				}
			}
		}
		return matchedFields;
	}

	public static interface FieldsCriteria {

		boolean match(TerminalField terminalField);
	}

	public static class AllFieldsCriteria implements FieldsCriteria {

		private static AllFieldsCriteria instance = new AllFieldsCriteria();

		public static AllFieldsCriteria instance() {
			return instance;
		}

		@Override
		public boolean match(TerminalField terminalField) {
			return true;
		}
	}

	public static class EditableFieldsCriteria implements FieldsCriteria {

		private static EditableFieldsCriteria instance = new EditableFieldsCriteria();

		public static EditableFieldsCriteria instance() {
			return instance;
		}

		@Override
		public boolean match(TerminalField terminalField) {
			return terminalField.isEditable();
		}
	}

	public static class ModifiedFieldsCriteria implements FieldsCriteria {

		private static ModifiedFieldsCriteria instance = new ModifiedFieldsCriteria();

		public static ModifiedFieldsCriteria instance() {
			return instance;
		}

		@Override
		public boolean match(TerminalField terminalField) {
			return terminalField.isModified();
		}
	}

}
