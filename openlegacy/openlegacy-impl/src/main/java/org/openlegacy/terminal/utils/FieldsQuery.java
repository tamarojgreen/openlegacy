package org.openlegacy.terminal.utils;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FieldsQuery {

	public static List<TerminalField> queryFields(TerminalSnapshot snapshot, FieldsCriteria criteria) {
		List<TerminalRow> rows = snapshot.getRows();

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

		public boolean match(TerminalField terminalField) {
			return true;
		}
	}

	public static class EditableFieldsCriteria implements FieldsCriteria {

		private static EditableFieldsCriteria instance = new EditableFieldsCriteria();

		public static EditableFieldsCriteria instance() {
			return instance;
		}

		public boolean match(TerminalField terminalField) {
			return terminalField.isEditable();
		}
	}

	public static class ModifiedFieldsCriteria implements FieldsCriteria {

		private static ModifiedFieldsCriteria instance = new ModifiedFieldsCriteria();

		public static ModifiedFieldsCriteria instance() {
			return instance;
		}

		public boolean match(TerminalField terminalField) {
			return terminalField.isModified();
		}
	}

}
