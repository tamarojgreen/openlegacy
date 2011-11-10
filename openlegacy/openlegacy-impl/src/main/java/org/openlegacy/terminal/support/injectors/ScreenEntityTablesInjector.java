package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class ScreenEntityTablesInjector implements ScreenEntityDataInjector {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private FieldFormatter fieldFormatter;

	@Inject
	private ApplicationContext applicationContext;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen) {

		Map<String, TableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(terminalScreen,
				screenEntityClass);

		Set<String> tableFieldNames = tableDefinitions.keySet();

		for (String tableFieldName : tableFieldNames) {

			TableDefinition tableDefinition = tableDefinitions.get(tableFieldName);
			List<Object> rows = new ArrayList<Object>();

			List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			int startRow = tableDefinition.getStartRow();
			int endRow = tableDefinition.getEndRow();
			for (int currentRow = startRow; currentRow <= endRow; currentRow++) {

				Object row = applicationContext.getBean(tableDefinition.getTableClass());
				ScreenEntityFieldAccessor rowAccessor = new SimpleScreenEntityFieldAccessor(row);

				boolean keyIsEmpty = false;

				for (ColumnDefinition columnDefinition : columnDefinitions) {
					ScreenPosition screenPosition = SimpleScreenPosition.newInstance(currentRow,
							columnDefinition.getStartColumn());
					String cellText = getCellContent(terminalScreen, screenPosition, columnDefinition);
					if (columnDefinition.isKey() && cellText.length() == 0) {
						keyIsEmpty = true;
					}
					rowAccessor.setFieldValue(columnDefinition.getName(), cellText);

					TerminalField terminalField = terminalScreen.getField(screenPosition);
					rowAccessor.setTerminalField(columnDefinition.getName(), terminalField);
				}
				if (!keyIsEmpty) {
					rows.add(row);
				}
			}
			fieldAccessor.setFieldValue(tableFieldName, rows);
		}
	}

	private String getCellContent(TerminalScreen terminalScreen, ScreenPosition screenPosition, ColumnDefinition columnDefinition) {
		int length = columnDefinition.getEndColumn() - columnDefinition.getStartColumn() + 1;
		String columnText = terminalScreen.getText(screenPosition, length);
		columnText = fieldFormatter.format(columnText);
		return columnText;
	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}
}
