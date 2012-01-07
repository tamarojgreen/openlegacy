package org.openlegacy.terminal.modules.table;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("unchecked")
public class DefaultTerminalTableModule extends TerminalSessionModuleAdapter implements Table {

	private static final long serialVersionUID = 1L;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public <T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass) {

		TableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				screenEntityClass).getValue();

		TableCollector<TerminalSession, T> tableCollector = SpringUtil.getDefaultBean(applicationContext,
				tableDefinition.getTableCollector());
		return tableCollector.collectAll(getSession(), screenEntityClass, rowClass);
	}

	/**
	 * Method which should be used when NOT using open legacy navigation definitions: <code>@ScreenNavigation</code> and using a
	 * provider session navigator
	 */
	public <T> T drillDown(Class<?> sourceEntityClass, Class<T> targetEntityClass, DrilldownAction<?> drilldownAction,
			Object... rowKeys) {

		if (ProxyUtil.isClassesMatch(getSession().getEntity().getClass(), targetEntityClass)) {
			return (T)getSession().getEntity();
		}

		TableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				sourceEntityClass).getValue();
		DrilldownDefinition drilldownDefinition = tableDefinition.getDrilldownDefinition();

		TableDrilldownPerformer<TerminalSession> actualDrilldownPerformer = SpringUtil.getDefaultBean(applicationContext,
				drilldownDefinition.getDrilldownPerformer());

		return actualDrilldownPerformer.drilldown(drilldownDefinition, getSession(), sourceEntityClass, targetEntityClass,
				drilldownAction, rowKeys);
	}

	/**
	 * Method which should be used when using open legacy navigation definitions: <code>@ScreenNavigation</code>
	 */
	public <T> T drillDown(Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys) throws RegistryException {
		NavigationDefinition navigationDefinition = screenEntitiesRegistry.get(targetClass).getNavigationDefinition();
		if (navigationDefinition == null) {
			throw (new RegistryException(targetClass.getName() + " has no navigation definition"));
		}
		Class<?> accessedFrom = navigationDefinition.getAccessedFrom();

		return drillDown(accessedFrom, targetClass, drilldownAction, rowKeys);
	}
}
