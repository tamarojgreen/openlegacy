package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
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

	public <T> List<T> collectAll(Class<?> screenEntityClass, Class<T> rowClass) {

		TableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				screenEntityClass).getValue();

		TableCollector<TerminalSession, T> tableCollector = SpringUtil.getDefaultBean(applicationContext,
				tableDefinition.getTableCollector());
		return tableCollector.collectAll(getSession(), screenEntityClass, rowClass);
	}

	public <T> T drillDown(Class<?> sourceEntityClass, Class<T> targetEntityClass, DrilldownAction<?> drilldownAction,
			Object... rowKeys) {
		TableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				sourceEntityClass).getValue();
		DrilldownDefinition drilldownDefinition = tableDefinition.getDrilldownDefinition();

		TableDrilldownPerformer<TerminalSession> actualDrilldownPerformer = SpringUtil.getDefaultBean(applicationContext,
				drilldownDefinition.getDrilldownPerformer());

		return actualDrilldownPerformer.drilldown(drilldownDefinition, getSession(), sourceEntityClass, targetEntityClass,
				drilldownAction, rowKeys);
	}
}
