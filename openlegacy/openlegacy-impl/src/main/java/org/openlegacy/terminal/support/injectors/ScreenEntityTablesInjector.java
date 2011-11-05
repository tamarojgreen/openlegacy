package org.openlegacy.terminal.support.injectors;

import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.ColumnDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ScreenEntityTablesInjector implements ScreenEntityDataInjector {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ApplicationContext applicationContext;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen,
			boolean deep) {

		Collection<TableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(terminalScreen,
				screenEntityClass);

		for (TableDefinition tableDefinition : tableDefinitions) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			List<Object> rows = new ArrayList();

			List<ColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			int startRow = tableDefinition.getStartRow();
			int endRow = tableDefinition.getEndRow();
			for (int currentRow = startRow; currentRow <= endRow; currentRow++) {

				Object row = applicationContext.getBean(tableDefinition.getRowClass());
				ScreenEntityFieldAccessor rowAccessor = new ScreenEntityDirectFieldAccessor(row);

				for (ColumnDefinition columnDefinition : columnDefinitions) {
					ScreenPosition screenPosition = SimpleScreenPosition.newInstance(currentRow,
							columnDefinition.getStartColumn());
					int length = columnDefinition.getEndColumn() - columnDefinition.getStartColumn();
					String columnText = terminalScreen.getText(screenPosition, length);
					rowAccessor.setFieldValue(columnDefinition.getName(), columnText);
				}
				rows.add(row);
			}
			fieldAccessor.setFieldValue(tableDefinition.getName(), rows);
		}
	}
}
