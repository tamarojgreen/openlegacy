package org.openlegacy.designtime.model;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableBuilder {

	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "column";

	public static TableDefinition toTableDefinition(List<TableColumn> tableColumns) {

		Collections.sort(tableColumns, new ColumnSorter());

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(null);
		for (int i = 0; i < tableColumns.size(); i++) {
			TableColumn tableColumn = tableColumns.get(i);

			// header
			TerminalField headerField = tableColumn.getFields().get(0);
			// 1st column cell
			TerminalField firstCellField = tableColumn.getFields().get(1);

			String columnName = headerField.getValue();
			if (StringUtil.getLength(columnName) == 0) {
				// if it is the 1st column and the field is editable in the size of 1-2, it's probably a selection field
				if (i == 0 && firstCellField.isEditable() && firstCellField.getLength() <= 2) {
					columnName = SELECTION_FIELD;
				} else {
					columnName = COLUMN + i;
				}
			}
			SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(StringUtil.toJavaFieldName(columnName));

			columnDefinition.setStartColumn(firstCellField.getPosition().getColumn());
			columnDefinition.setEndColumn(columnDefinition.getStartColumn() + firstCellField.getLength() - 1);
			columnDefinition.setDisplayName(StringUtil.toDisplayName(columnName));
			columnDefinition.setSampleValue(firstCellField.getValue());
			columnDefinition.setEditable(firstCellField.isEditable());

			tableDefinition.getColumnDefinitions().add(columnDefinition);
		}
		// The 2nd field (1st is header) in the 1st column
		TerminalField topLeftTableCell = tableColumns.get(0).getFields().get(1);
		tableDefinition.setStartRow(topLeftTableCell.getPosition().getRow());
		List<TerminalField> lastColumnFields = tableColumns.get(tableColumns.size() - 1).getFields();
		tableDefinition.setEndRow(lastColumnFields.get(lastColumnFields.size() - 1).getPosition().getRow());
		return tableDefinition;
	}

	public static class ColumnSorter implements Comparator<TableColumn> {

		public int compare(TableColumn o1, TableColumn o2) {
			return o1.getFields().get(0).getPosition().getColumn() - o2.getFields().get(0).getPosition().getColumn();
		}

	}
}
