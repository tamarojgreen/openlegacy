package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class TableBuilder {

	private static final String ROW = "Row";
	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "Column";

	private final static Log logger = LogFactory.getLog(TableBuilder.class);

	public static void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TableColumn> tableColumns) {

		Collections.sort(tableColumns, ColumnComparator.instance());

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(null);

		TerminalField topLeftTableCell = tableColumns.get(0).getFields().get(0);

		// ignore the table if it's outside a defined window border
		if (!screenEntityDefinition.getSnapshotBorders().contains(topLeftTableCell.getPosition(), false)) {
			logger.info(MessageFormat.format("Table which starts at {0} found outside window. Ignoring it. Screen: {1}",
					topLeftTableCell.getPosition(), screenEntityDefinition.getEntityName()));
			return;
		}

		for (int i = 0; i < tableColumns.size(); i++) {
			TableColumn tableColumn = tableColumns.get(i);

			TerminalField headerField = getHeader(tableColumn);

			// 1st column cell
			TerminalField firstCellField = tableColumn.getFields().get(0);

			String columnName = headerField != null ? headerField.getValue() : null;
			if (StringUtil.getLength(columnName) == 0) {
				// if it is the 1st column and the field is editable in the size of 1-2, it's probably a selection field
				if (isSelectionField(i, firstCellField)) {
					columnName = SELECTION_FIELD;
				} else {
					columnName = COLUMN + (i + 1);
				}
			}
			ColumnDefinition columnDefinition = initColumn(i, firstCellField, columnName);

			tableDefinition.getColumnDefinitions().add(columnDefinition);

			// remove the fields from the snapshot to avoid re-recognize by other rules
			screenEntityDefinition.getSnapshot().getFields().removeAll(tableColumn.getFields());
			if (headerField != null) {
				screenEntityDefinition.getSnapshot().getFields().remove(headerField);
			}

		}

		tableDefinition.setStartRow(topLeftTableCell.getPosition().getRow());
		List<TerminalField> lastColumnFields = tableColumns.get(tableColumns.size() - 1).getFields();
		tableDefinition.setEndRow(lastColumnFields.get(lastColumnFields.size() - 1).getPosition().getRow());

		// TODO handle multiple table in screen table name
		int tableCount = screenEntityDefinition.getTableDefinitions().size();
		String tableSuffix = tableCount == 0 ? "" : String.valueOf(tableCount - 1);
		tableDefinition.setTableEntityName(MessageFormat.format("{0}{1}{2}", screenEntityDefinition.getEntityName(), ROW,
				tableSuffix));
		screenEntityDefinition.getTableDefinitions().put(tableDefinition.getTableEntityName(), tableDefinition);

		logger.info(MessageFormat.format("Added table {0} to screen entity {1}", tableDefinition.getTableEntityName(),
				screenEntityDefinition.getEntityName()));

	}

	private static TerminalField getHeader(TableColumn tableColumn) {
		List<TerminalField> headerFields = tableColumn.getHeaderFields();
		// TODO handle multiple header line
		TerminalField headerField = null;
		if (headerFields.size() > 0) {
			headerField = headerFields.get(0);
		}
		return headerField;
	}

	private static SimpleColumnDefinition initColumn(int i, TerminalField firstCellField, String columnName) {
		SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(StringUtil.toJavaFieldName(columnName));

		columnDefinition.setStartColumn(firstCellField.getPosition().getColumn());
		columnDefinition.setEndColumn(columnDefinition.getStartColumn() + firstCellField.getLength() - 1);
		columnDefinition.setDisplayName(StringUtil.toDisplayName(columnName));
		columnDefinition.setSampleValue(StringUtils.trim(firstCellField.getValue()));
		columnDefinition.setEditable(firstCellField.isEditable());

		columnDefinition.setSelectionField(isSelectionField(i, firstCellField));
		return columnDefinition;
	}

	private static boolean isSelectionField(int columnIndex, TerminalField firstCellField) {
		return columnIndex == 0 && firstCellField.isEditable() && firstCellField.getLength() <= 2;
	}
}
