package org.openlegacy.terminal.modules.table;

import org.openlegacy.HostAction;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class DefaultTableModule extends TerminalSessionModuleAdapter implements Table {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private HostAction defaultNextScreenAction;

	@SuppressWarnings("unused")
	private HostAction defaultPreviousScreenAction;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass) {

		ScreenEntityDefinition screenEntityDefintion = screenEntitiesRegistry.get(screenEntityClass);
		Map<String, TableDefinition> tableDefinitionsMap = screenEntityDefintion.getTableDefinitions();
		Set<String> tableNames = tableDefinitionsMap.keySet();

		TableDefinition matchingTableDefinition = null;
		String matchingTableFieldName = null;

		for (String tableName : tableNames) {

			TableDefinition tableDefinition = tableDefinitionsMap.get(tableName);
			if (tableDefinition.getTableClass() == rowClass) {
				matchingTableFieldName = tableName;
				matchingTableDefinition = tableDefinition;
				break;
			}
		}

		if (matchingTableDefinition == null) {
			throw (new IllegalArgumentException(MessageFormat.format("Could not found table of type {0} in {1}", rowClass,
					screenEntityDefintion)));
		}

		int rowsCount = matchingTableDefinition.getEndRow() - matchingTableDefinition.getStartRow() + 1;

		List allRows = new ArrayList();

		List<?> rows = null;
		boolean cont = true;

		Object screenEntity = getTerminalSession().getEntity(screenEntityClass);
		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		while (cont) {

			rows = (List<?>)fieldAccessor.getFieldValue(matchingTableFieldName);
			allRows.addAll(rows);

			// more rows exists
			if (rows.size() < rowsCount) {
				cont = false;
			} else {
				screenEntity = getTerminalSession().doAction(defaultNextScreenAction, null);
				fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);
			}
		}

		return allRows;
	}

	public void setDefaultNextScreenAction(Class<? extends HostAction> defaultNextScreenAction) {
		this.defaultNextScreenAction = ReflectionUtil.newInstance(defaultNextScreenAction);
	}

	public void setDefaultPreviousScreenAction(Class<? extends HostAction> defaultPreviousScreenAction) {
		this.defaultPreviousScreenAction = ReflectionUtil.newInstance(defaultPreviousScreenAction);
	}
}
