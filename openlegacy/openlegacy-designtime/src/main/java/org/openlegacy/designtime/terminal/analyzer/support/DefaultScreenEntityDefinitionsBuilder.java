package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.ScreeEntitynDefinitionsBuilder;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.definitions.SimpleTableDefinition;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultScreenEntityDefinitionsBuilder implements ScreeEntitynDefinitionsBuilder {

	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "Column";
	private static final String ROW = "Row";
	private int maxIdentifiers = 3;

	public void addIdentifier(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		// if the field was removed from the snapshot (convert to entity field/column) ignore it
		// drools analyze the fields in advance, and ignore fields removal done in RT
		if (!screenEntityDefinition.getSnapshot().getFields().contains(field)) {
			return;
		}

		if (identification.getScreenIdentifiers().size() >= maxIdentifiers) {
			return;
		}
		ScreenIdentifier identifier = new SimpleScreenIdentifier(field.getPosition(), field.getValue());
		identification.getScreenIdentifiers().add(identifier);

		Collections.sort(identification.getScreenIdentifiers(), IdentifierComparator.instance());
	}

	public void setScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		if (screenEntityDefinition.getEntityName() == null) {
			String entityName = StringUtil.toClassName(field.getValue());
			screenEntityDefinition.setEntityName(entityName);
			snapshotsAnalyzerContext.addEntityDefinition(screenEntityDefinition);
		}

	}

	public void addEditableField(ScreenEntityDefinition screenEntityDefinition, TerminalField editableField, String leadingLabel) {

		String fieldName = StringUtil.toJavaFieldName(leadingLabel);
		SimpleFieldMappingDefinition fieldMappingDefinition = new SimpleFieldMappingDefinition(fieldName, null);
		fieldMappingDefinition.setScreenPosition(editableField.getPosition());
		fieldMappingDefinition.setLength(editableField.getLength());
		fieldMappingDefinition.setEditable(editableField.isEditable());
		fieldMappingDefinition.setDisplayName(StringUtil.toDisplayName(leadingLabel));
		fieldMappingDefinition.setSampleValue("");

		screenEntityDefinition.getFieldsDefinitions().put(fieldName, fieldMappingDefinition);

	}

	public void addTableDefinition(ScreenEntityDefinition screenEntityDefinition, List<TableColumn> tableColumns) {

		Collections.sort(tableColumns, ColumnComparator.instance());

		SimpleTableDefinition tableDefinition = new SimpleTableDefinition(null);
		for (int i = 0; i < tableColumns.size(); i++) {
			TableColumn tableColumn = tableColumns.get(i);

			List<TerminalField> headerFields = tableColumn.getHeaderFields();
			// TODO handle multiple header line
			TerminalField headerField = null;
			if (headerFields.size() > 0) {
				headerField = headerFields.get(0);
			}

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
			SimpleColumnDefinition columnDefinition = new SimpleColumnDefinition(StringUtil.toJavaFieldName(columnName));

			columnDefinition.setStartColumn(firstCellField.getPosition().getColumn());
			columnDefinition.setEndColumn(columnDefinition.getStartColumn() + firstCellField.getLength() - 1);
			columnDefinition.setDisplayName(StringUtil.toDisplayName(columnName));
			columnDefinition.setSampleValue(StringUtils.trim(firstCellField.getValue()));
			columnDefinition.setEditable(firstCellField.isEditable());

			columnDefinition.setSelectionField(isSelectionField(i, firstCellField));

			tableDefinition.getColumnDefinitions().add(columnDefinition);
		}
		TerminalField topLeftTableCell = tableColumns.get(0).getFields().get(0);
		tableDefinition.setStartRow(topLeftTableCell.getPosition().getRow());
		List<TerminalField> lastColumnFields = tableColumns.get(tableColumns.size() - 1).getFields();
		tableDefinition.setEndRow(lastColumnFields.get(lastColumnFields.size() - 1).getPosition().getRow());

		// TODO handle multiple table in screen table name
		int tableCount = screenEntityDefinition.getTableDefinitions().size();
		String tableSuffix = tableCount == 0 ? "" : String.valueOf(tableCount - 1);
		tableDefinition.setTableEntityName(MessageFormat.format("{0}{1}{2}", screenEntityDefinition.getEntityName(), ROW,
				tableSuffix));
		screenEntityDefinition.getTableDefinitions().put(tableDefinition.getTableEntityName(), tableDefinition);

	}

	private static boolean isSelectionField(int columnIndex, TerminalField firstCellField) {
		return columnIndex == 0 && firstCellField.isEditable() && firstCellField.getLength() <= 2;
	}

	public static class ColumnComparator implements Comparator<TableColumn> {

		private static ColumnComparator instance = new ColumnComparator();

		public static ColumnComparator instance() {
			return instance;
		}

		public int compare(TableColumn o1, TableColumn o2) {
			return o1.getFields().get(0).getPosition().getColumn() - o2.getFields().get(0).getPosition().getColumn();
		}

	}

	public static class IdentifierComparator implements Comparator<ScreenIdentifier> {

		private static IdentifierComparator instance = new IdentifierComparator();

		public static IdentifierComparator instance() {
			return instance;
		}

		public int compare(ScreenIdentifier o1, ScreenIdentifier o2) {
			ScreenPosition position1 = ((SimpleScreenIdentifier)o1).getPosition();
			ScreenPosition position2 = ((SimpleScreenIdentifier)o2).getPosition();

			if (position1.getRow() != position2.getRow()) {
				return position1.getRow() - position2.getRow();
			}
			return position1.getColumn() - position2.getColumn();
		}

	}

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}
}
