package org.openlegacy.terminal.modules.table;

import org.openlegacy.HostAction;
import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerfomer;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
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

	@Inject
	private TableDrilldownPerfomer<TerminalSession> tableDrilldownPerfomer;

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
			throw (new IllegalArgumentException(MessageFormat.format("Could not find table of type {0} in {1}", rowClass,
					screenEntityDefintion)));
		}

		int rowsCount = matchingTableDefinition.getEndRow() - matchingTableDefinition.getStartRow() + 1;

		List allRows = new ArrayList();

		List<?> rows = null;
		boolean cont = true;

		Object screenEntity = getTerminalSession().getEntity(screenEntityClass);
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		while (cont) {

			rows = (List<?>)fieldAccessor.getFieldValue(matchingTableFieldName);
			allRows.addAll(rows);

			// more rows exists
			if (rows.size() < rowsCount) {
				cont = false;
			} else {
				screenEntity = getTerminalSession().doAction(defaultNextScreenAction, null);
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
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

	public <T> T drillDown(Class<?> sourceEntityClass, Class<T> targetEntityClass, DrilldownAction drilldownAction,
			Object... rowKeys) {
		return tableDrilldownPerfomer.drilldown(getTerminalSession(), sourceEntityClass, targetEntityClass, drilldownAction,
				rowKeys);
	}
}
