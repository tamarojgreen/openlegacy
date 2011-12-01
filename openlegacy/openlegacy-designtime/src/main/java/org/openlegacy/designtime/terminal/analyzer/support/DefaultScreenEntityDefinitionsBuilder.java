package org.openlegacy.designtime.terminal.analyzer.support;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.designtime.analyzer.SnapshotsAnalyzerContext;
import org.openlegacy.designtime.terminal.analyzer.ScreeEntitynDefinitionsBuilder;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.TableColumn;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultScreenEntityDefinitionsBuilder implements ScreeEntitynDefinitionsBuilder {

	private static final String SELECTION_FIELD = "Selection";
	private static final String COLUMN = "Column";
	private static final String ROW = "Row";
	private int maxIdentifiers = 3;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityDefinitionsBuilder.class);

	public void addIdentifier(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		ScreenIdentification identification = screenEntityDefinition.getScreenIdentification();

		// if the field was removed from the snapshot (convert to entity field/column) ignore it
		// drools analyze the fields in advance, and ignore fields removal done in RT
		if (!screenEntityDefinition.getSnapshot().getFields().contains(field)) {
			return;
		}

		List<ScreenIdentifier> screenIdentifiers = identification.getScreenIdentifiers();
		if (screenIdentifiers.size() >= maxIdentifiers) {
			return;
		}
		ScreenIdentifier identifier = new SimpleScreenIdentifier(field.getPosition(), field.getValue());
		screenIdentifiers.add(identifier);

		Collections.sort(screenIdentifiers, ScreenPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added identifier \"{0}\" at position {1} to screen {2}", field.getValue(),
				field.getPosition(), screenEntityDefinition.getEntityName()));
	}

	public void setScreenEntityName(
			SnapshotsAnalyzerContext<TerminalSnapshot, ScreenEntityDesigntimeDefinition> snapshotsAnalyzerContext,
			ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalField field) {

		if (screenEntityDefinition.getEntityName() == null) {
			String entityName = StringUtil.toClassName(field.getValue());
			screenEntityDefinition.setEntityName(entityName);
			snapshotsAnalyzerContext.addEntityDefinition(screenEntityDefinition);
			logger.info(MessageFormat.format("New screen entity add: {0}", entityName));
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

		logger.info(MessageFormat.format("Added editable field {0} to screen entity {1}", fieldName,
				screenEntityDefinition.getEntityName()));
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

			// remove the fields from the snapshot to avoid re-recognize by other rules
			screenEntityDefinition.getSnapshot().getFields().removeAll(tableColumn.getFields());

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

		logger.info(MessageFormat.format("Added table {0} to screen entity {1}", tableDefinition.getTableEntityName(),
				screenEntityDefinition.getEntityName()));

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

	public void setMaxIdentifiers(int maxIdentifiers) {
		this.maxIdentifiers = maxIdentifiers;
	}

	@SuppressWarnings("unchecked")
	public void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, String text, String regex) {

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(text);

		match.find();
		if (match.groupCount() < 2) {
			logger.warn(MessageFormat.format("text is not in the format of: action -> displayName", text,
					screenEntityDefinition.getEntityName()));
			return;
		}

		Class<? extends SessionAction<Session>> actionClass = null;
		try {
			actionClass = (Class<? extends SessionAction<Session>>)Class.forName(MessageFormat.format("{0}{1}{2}",
					TerminalActions.class.getName(), ClassUtils.INNER_CLASS_SEPARATOR, match.group(1)));
		} catch (ClassNotFoundException e) {
			logger.warn(MessageFormat.format("Could not found class for Action {0} in screen {1}", text,
					screenEntityDefinition.getEntityName()));
			return;
		}

		ActionDefinition actionDefinition = new SimpleActionDefinition(actionClass, match.group(2));

		screenEntityDefinition.getActions().add(actionDefinition);

		logger.info(MessageFormat.format("Added action {0}:{1} to screen entity {2}", actionDefinition.getAction().getName(),
				actionDefinition.getDisplayName(), screenEntityDefinition.getEntityName()));
	}

	public static class ScreenPositionContainerComparator implements Comparator<ScreenPositionContainer> {

		private static ScreenPositionContainerComparator instance = new ScreenPositionContainerComparator();

		public static ScreenPositionContainerComparator instance() {
			return instance;
		}

		public int compare(ScreenPositionContainer o1, ScreenPositionContainer o2) {
			ScreenPosition position1 = o1.getPosition();
			ScreenPosition position2 = o2.getPosition();

			if (position1.getRow() != position2.getRow()) {
				return position1.getRow() - position2.getRow();
			}
			return position1.getColumn() - position2.getColumn();
		}

	}

	public TableColumn addTableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {
		TerminalSnapshot snapshot = screenEntityDefinition.getSnapshot();
		// if the fields were removed from the snapshot, don't create a column
		if (!snapshot.getFields().contains(fields.get(0))) {
			return null;
		}
		TableColumn tableColumn = new TableColumn(screenEntityDefinition, fields);

		logger.info(MessageFormat.format("Recognized column \n{0} in screen entity {1}", tableColumn,
				screenEntityDefinition.getEntityName()));

		return tableColumn;

	}

}
