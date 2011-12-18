package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ColumnComparator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class ScreenTableFactProcessor implements ScreenFactProcessor {

	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "Column";

	private final static Log logger = LogFactory.getLog(ScreenTableFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ScreenTableFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenTableFact screenTableFact = (ScreenTableFact)screenFact;

		addTableDefinition(screenEntityDefinition, screenTableFact.getTableColumns());

	}

	public static void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition,
			List<TableColumnFact> TableColumnFacts) {

		Collections.sort(TableColumnFacts, ColumnComparator.instance());

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(null);

		List<TerminalField> firstColumnFields = TableColumnFacts.get(0).getFields();
		TerminalField topLeftTableCell = firstColumnFields.get(0);

		// ignore the table if it's outside a defined window border
		if (!screenEntityDefinition.getSnapshotBorders().contains(topLeftTableCell.getPosition(), false)) {
			logger.info(MessageFormat.format("Table which starts at {0} found outside window. Ignoring it. Screen: {1}",
					topLeftTableCell.getPosition(), screenEntityDefinition.getEntityName()));
			return;
		}

		for (int i = 0; i < TableColumnFacts.size(); i++) {
			TableColumnFact TableColumnFact = TableColumnFacts.get(i);

			TerminalField headerField = getHeader(TableColumnFact);

			// 1st column cell
			TerminalField firstCellField = TableColumnFact.getFields().get(0);

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
			screenEntityDefinition.getSnapshot().getFields().removeAll(TableColumnFact.getFields());
			if (headerField != null) {
				screenEntityDefinition.getSnapshot().getFields().remove(headerField);
			}

		}

		tableDefinition.setStartRow(topLeftTableCell.getPosition().getRow());
		tableDefinition.setEndRow(firstColumnFields.get(firstColumnFields.size() - 1).getPosition().getRow());

		// add table to the screen entity without a name, which will be given by the screen entity name at a later phase
		screenEntityDefinition.addTemporaryTable(tableDefinition);

		logger.info(MessageFormat.format("Added table {0} to screen entity {1}", tableDefinition.getTableEntityName(),
				screenEntityDefinition.getEntityName()));

	}

	private static TerminalField getHeader(TableColumnFact TableColumnFact) {
		List<TerminalField> headerFields = TableColumnFact.getHeaderFields();
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
