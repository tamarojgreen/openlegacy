package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.DrilldownException;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.TableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.List;

import javax.inject.Inject;

/**
 * Default terminal session drill down performer. Fetch the source entity class table definitions, looks from a row in the given
 * screen, and performs scroll until the row is found
 * 
 */
public class DefaultTableDrilldownPerformer implements TableDrilldownPerformer<TerminalSession> {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public <T> T drilldown(DrilldownDefinition drilldownDefinition, TerminalSession session, Class<?> sourceEntityClass,
			Class<T> targetEntityClass, DrilldownAction drilldownAction, Object... rowKeys) {

		RowFinder rowFinder = getDefaultBean(drilldownDefinition.getRowFinder());

		RowSelector<TerminalSession> rowSelector = getDefaultBean(drilldownDefinition.getRowSelector());

		TableScroller<TerminalSession> tableScroller = getDefaultBean(drilldownDefinition.getTableScroller());

		RowComparator rowComparator = getDefaultBean(drilldownDefinition.getRowComparator());

		TableScrollStopConditions tableScrollStopConditions = getDefaultBean(drilldownDefinition.getTableScrollStopCondition());

		String tableFieldName = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider, sourceEntityClass).getKey();

		ScreenEntity currentEntity = (ScreenEntity)session.getEntity(sourceEntityClass);

		ScreenPojoFieldAccessor fieldAccessor = null;

		Integer rowNumber = null;

		do {
			fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
			List<?> tableRows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);
			rowNumber = rowFinder.findRow(rowComparator, tableRows, rowKeys);
			if (rowNumber == null) {
				currentEntity = (ScreenEntity)tableScroller.scroll(session, sourceEntityClass, tableScrollStopConditions, rowKeys);
			}
		} while (rowNumber == null && currentEntity != null);

		if (rowNumber != null) {
			rowSelector.selectRow(session, currentEntity, drilldownAction, rowNumber);
			return session.getEntity(targetEntityClass);
		}
		throw (new DrilldownException("Unable to drilldown into " + targetEntityClass + " with keys:" + rowKeys));
	}

	private <T> T getDefaultBean(Class<T> clazz) {
		return SpringUtil.getDefaultBean(applicationContext, clazz);
	}
}
