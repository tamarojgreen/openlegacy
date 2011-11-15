package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.DrilldownException;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerfomer;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;

import javax.inject.Inject;

/**
 * Default terminal session drill down performer. Fetch the source entity class table definitions, looks from a row in the given
 * screen, and performs scroll until the row is found
 * 
 */
public class DefaultTableDrilldownPerformer implements TableDrilldownPerfomer<TerminalSession> {

	@Inject
	private RowFinder rowFinder;

	@Inject
	private TableScroller<TerminalSession> tableScroller;

	@Inject
	private RowSelector<TerminalSession> rowSelector;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	public <T> T drilldown(TerminalSession session, Class<?> sourceEntityClass, Class<T> targetEntityClass,
			DrilldownAction drilldownAction, Object... rowKeys) {

		String tableFieldName = TableUtil.getSingleTableDefinition(tablesDefinitionProvider, sourceEntityClass).getKey();

		ScreenEntity currentEntity = (ScreenEntity)session.getEntity(sourceEntityClass);

		ScreenPojoFieldAccessor fieldAccessor = null;

		Integer rowNumber = null;

		do {
			fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);
			List<?> tableRows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);
			rowNumber = rowFinder.findRow(tableRows, rowKeys);
			if (rowNumber == null) {
				currentEntity = (ScreenEntity)tableScroller.scroll(session, sourceEntityClass, rowKeys);
			}
		} while (rowNumber == null && currentEntity != null);

		if (rowNumber != null) {
			rowSelector.selectRow(session, currentEntity, drilldownAction, rowNumber);
			return session.getEntity(targetEntityClass);
		}
		throw (new DrilldownException("Unable to drilldown into " + targetEntityClass + " with keys:" + rowKeys));
	}

	public void setRowFinder(RowFinder rowFinder) {
		this.rowFinder = rowFinder;
	}
}
