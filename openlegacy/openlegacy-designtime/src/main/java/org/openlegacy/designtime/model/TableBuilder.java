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

	public static TableDefinition toTableDefinition(List<TableColumn> tableColumns) {

		Collections.sort(tableColumns, new ColumnSorter());

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(null);
		for (TableColumn tableColumn : tableColumns) {
			TerminalField firstField = tableColumn.getFields().get(0);
			TerminalField secondField = tableColumn.getFields().get(1);

			SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(
					StringUtil.toJavaFieldName(firstField.getValue()));

			columnDefinition.setStartColumn(secondField.getPosition().getColumn());
			columnDefinition.setEndColumn(columnDefinition.getStartColumn() + secondField.getLength() - 1);
			columnDefinition.setDisplayName(StringUtil.toDisplayName(firstField.getValue()));
			columnDefinition.setSampleValue(secondField.getValue());

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
