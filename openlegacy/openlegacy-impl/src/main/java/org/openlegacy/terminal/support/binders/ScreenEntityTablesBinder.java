package org.openlegacy.terminal.support.binders;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

@Component
public class ScreenEntityTablesBinder implements ScreenEntityBinder {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private ApplicationContext applicationContext;

	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Map<String, TableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(screenEntity.getClass());

		Set<String> tableFieldNames = tableDefinitions.keySet();

		for (String tableFieldName : tableFieldNames) {

			TableDefinition tableDefinition = tableDefinitions.get(tableFieldName);
			List<Object> rows = new ArrayList<Object>();

			List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			int startRow = tableDefinition.getStartRow();
			int endRow = tableDefinition.getEndRow();
			for (int currentRow = startRow; currentRow <= endRow; currentRow++) {

				Object row = applicationContext.getBean(tableDefinition.getTableClass());
				ScreenPojoFieldAccessor rowAccessor = new SimpleScreenPojoFieldAccessor(row);

				boolean keyIsEmpty = false;

				for (ColumnDefinition columnDefinition : columnDefinitions) {
					ScreenPosition screenPosition = SimpleScreenPosition.newInstance(currentRow,
							columnDefinition.getStartColumn());
					String cellText = getCellContent(terminalSnapshot, screenPosition, columnDefinition);
					if (columnDefinition.isKey() && cellText.length() == 0) {
						keyIsEmpty = true;
					}
					rowAccessor.setFieldValue(columnDefinition.getName(), cellText);

					TerminalField terminalField = terminalSnapshot.getField(screenPosition);
					rowAccessor.setTerminalField(columnDefinition.getName(), terminalField);
				}
				if (!keyIsEmpty) {
					rows.add(row);
				}
			}
			fieldAccessor.setFieldValue(tableFieldName, rows);
		}
	}

	private String getCellContent(TerminalSnapshot terminalSnapshot, ScreenPosition screenPosition,
			ColumnDefinition columnDefinition) {
		int length = columnDefinition.getEndColumn() - columnDefinition.getStartColumn() + 1;
		String columnText = terminalSnapshot.getText(screenPosition, length);
		columnText = fieldFormatter.format(columnText);
		return columnText;
	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalScreen, Object entity) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);

		Map<String, TableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(entity.getClass());

		Set<String> tableFieldNames = tableDefinitions.keySet();

		for (String tableFieldName : tableFieldNames) {
			TableDefinition tableDefinition = tableDefinitions.get(tableFieldName);
			List<?> rows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);

			int rowCount = 0;
			for (Object row : rows) {
				List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
				fieldAccessor = new SimpleScreenPojoFieldAccessor(row);
				for (ColumnDefinition columnDefinition : columnDefinitions) {
					if (columnDefinition.isEditable()) {
						Object value = fieldAccessor.getFieldValue(columnDefinition.getName());
						if (value == null) {
							continue;
						}
						String valueString = value.toString();
						if (StringUtils.hasLength(valueString)) {
							int screenRow = tableDefinition.getStartRow() + rowCount;
							TerminalField terminalField = terminalScreen.getField(SimpleScreenPosition.newInstance(screenRow,
									columnDefinition.getStartColumn()));
							terminalField.setValue(valueString);
							sendAction.getModifiedFields().add(terminalField);
						}
					}

				}
				rowCount++;
			}

		}

	}
}
