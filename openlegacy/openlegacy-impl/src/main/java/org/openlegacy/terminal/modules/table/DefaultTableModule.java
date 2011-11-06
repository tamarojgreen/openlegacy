package org.openlegacy.terminal.modules.table;

import org.openlegacy.HostAction;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DefaultTableModule extends TerminalSessionModuleAdapter implements Table {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private HostAction defaultNextScreenAction;

	@SuppressWarnings("unused")
	private HostAction defaultPreviousScreenAction;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass) {

		ScreenEntityDefinition screenEntityDefintions = screenEntitiesRegistry.get(screenEntityClass);
		Map<Class<?>, TableDefinition> tablesDefinitions = screenEntityDefintions.getTableDefinitions();
		TableDefinition tableDefinition = tablesDefinitions.get(rowClass);

		int rowsCount = tableDefinition.getEndRow() - tableDefinition.getStartRow() + 1;

		List allRows = new ArrayList();

		List<?> rows = null;
		boolean cont = true;

		Object screenEntity = getTerminalSession().getEntity(screenEntityClass);
		ScreenEntityFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);

		while (cont) {

			rows = (List<?>)fieldAccessor.getFieldValue(tableDefinition.getName());
			allRows.addAll(rows);

			// more rows exists
			if (rows.size() < rowsCount) {
				cont = false;
			} else {
				screenEntity = getTerminalSession().doAction(defaultNextScreenAction, null);
				fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);
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
