package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.DrilldownException;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.table.ScreenTableDrilldownPerformer;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

/**
 * Default terminal session drill down performer. Fetch the source entity class table definitions, looks from a row in the given
 * screen, and performs scroll until the row is found
 * 
 */
public class DefaultTableDrilldownPerformer implements ScreenTableDrilldownPerformer {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ApplicationContext applicationContext;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T drilldown(DrilldownDefinition drilldownDefinition, TerminalSession session, Class<?> sourceEntityClass,
			Class<T> targetEntityClass, DrilldownAction<?> drilldownAction, Object... rowKeys) {

		RowFinder rowFinder = getDefaultBean(drilldownDefinition.getRowFinder());

		RowSelector rowSelector = getDefaultBean(drilldownDefinition.getRowSelector());

		TableScroller tableScroller = getDefaultBean(drilldownDefinition.getTableScroller());

		RowComparator rowComparator = getDefaultBean(drilldownDefinition.getRowComparator());

		TableScrollStopConditions tableScrollStopConditions = getDefaultBean(drilldownDefinition.getTableScrollStopCondition());

		Entry<String, ScreenTableDefinition> singleScrollableTableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, sourceEntityClass);
		ScreenTableDefinition tableDefinition = singleScrollableTableDefinition.getValue();
		String tableFieldName = singleScrollableTableDefinition.getKey();

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
		throw (new DrilldownException("Unable to drilldown into " + targetEntityClass + ", with key field: "
				+ Arrays.toString(tableDefinition.getKeyFieldNames().toArray()) + " with keys values:" + Arrays.toString(rowKeys)));
	}

	private <T> T getDefaultBean(Class<T> clazz) {
		return SpringUtil.getDefaultBean(applicationContext, clazz);
	}
}
