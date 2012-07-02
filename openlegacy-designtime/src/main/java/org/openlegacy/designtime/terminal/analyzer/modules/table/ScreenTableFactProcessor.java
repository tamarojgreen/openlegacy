package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FieldFormatter;
import org.openlegacy.designtime.analyzer.TextTranslator;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ColumnComparator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.table.RecordSelectionEntity;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenTableDefinition;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ScreenTableFactProcessor implements ScreenFactProcessor {

	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "Column";

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private TextTranslator textTranslator;

	private final static Log logger = LogFactory.getLog(ScreenTableFactProcessor.class);

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ScreenTableFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenTableFact screenTableFact = (ScreenTableFact)screenFact;

		addTableDefinition(screenEntityDefinition, screenTableFact.getTableColumns());

	}

	public void addTableDefinition(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TableColumnFact> TableColumnFacts) {

		Collections.sort(TableColumnFacts, ColumnComparator.instance());

		SimpleScreenTableDefinition tableDefinition = new SimpleScreenTableDefinition(null);

		List<TerminalField> firstColumnFields = TableColumnFacts.get(0).getFields();
		TerminalField topLeftTableCell = firstColumnFields.get(0);

		// ignore the table if it's outside a defined window border
		if (!screenEntityDefinition.getSnapshotBorders().contains(topLeftTableCell.getPosition(), false)) {
			logger.info(MessageFormat.format("Table which starts at {0} found outside window. Ignoring it. Screen: {1}",
					topLeftTableCell.getPosition(), screenEntityDefinition.getEntityName()));
			return;
		}

		List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
		for (int i = 0; i < TableColumnFacts.size(); i++) {
			TableColumnFact tableColumnFact = TableColumnFacts.get(i);

			TerminalField headerField = getHeader(tableColumnFact);

			// 1st column cell
			TerminalField firstCellField = tableColumnFact.getFields().get(0);

			String columnName = headerField != null ? headerField.getValue() : null;

			columnName = fieldFormatter.format(columnName);

			if (StringUtil.getLength(columnName) == 0) {
				// if it is the 1st column and the field is editable in the size of 1-2, it's probably a selection field
				if (isSelectionField(i, firstCellField)) {
					columnName = SELECTION_FIELD;
				} else {
					columnName = COLUMN + (i + 1);
				}
			}
			SimpleScreenColumnDefinition columnDefinition = initColumn(i, firstCellField, columnName);

			// define the screen as record selection type if it has a selection field and it's NOT window
			if (columnDefinition.isSelectionField() && !screenEntityDefinition.isWindow()) {
				screenEntityDefinition.setType(RecordSelectionEntity.class);
				screenEntityDefinition.getReferredClasses().add(RecordSelectionEntity.class.getName());
			}

			if (!isSelectionField(i, firstCellField) && tableDefinition.getKeyFieldNames().size() == 0) {
				// mark the 1st non edit-able field as key (in design-time)
				// TODO logic needs to be more rich to check for most populated column
				columnDefinition.setKey(true);
			}

			if (tableDefinition.getKeyFieldNames().size() > 0 && tableDefinition.getMainDisplayField() == null) {
				// assume (in design-time) the 2nd column followed by key column in the main display column
				tableDefinition.setMainDisplayField(columnDefinition.getName());
			}

			columnDefinitions.add(columnDefinition);

			// remove the fields from the snapshot to avoid re-recognize by other rules
			screenEntityDefinition.getSnapshot().getFields().removeAll(tableColumnFact.getFields());
			if (headerField != null) {
				screenEntityDefinition.getSnapshot().getFields().remove(headerField);
			}

		}

		// set the last field as main display field if non was set - may happen if there only 2 columns - 1 action field
		if (tableDefinition.getMainDisplayField() == null) {
			tableDefinition.setMainDisplayField(columnDefinitions.get(columnDefinitions.size() - 1).getName());
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

	private SimpleScreenColumnDefinition initColumn(int coloumnIndex, TerminalField firstCellField, String columnName) {
		columnName = textTranslator.translate(columnName);
		SimpleScreenColumnDefinition columnDefinition = new SimpleScreenColumnDefinition(StringUtil.toJavaFieldName(columnName));

		columnDefinition.setStartColumn(firstCellField.getPosition().getColumn());
		columnDefinition.setEndColumn(columnDefinition.getStartColumn() + firstCellField.getLength() - 1);
		columnDefinition.setDisplayName(StringUtil.toDisplayName(columnName));
		columnDefinition.setSampleValue(StringUtils.trim(firstCellField.getValue()));
		columnDefinition.setEditable(firstCellField.isEditable());
		columnDefinition.setJavaType(firstCellField.getType());

		columnDefinition.setSelectionField(isSelectionField(coloumnIndex, firstCellField));

		return columnDefinition;
	}

	private static boolean isSelectionField(int columnIndex, TerminalField firstCellField) {
		return columnIndex == 0 && firstCellField.isEditable() && firstCellField.getLength() <= 2;
	}
}
