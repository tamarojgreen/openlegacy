package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.RowSelectionDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

/**
 * Default terminal row selection implementation. Extract a single table definition for the given entity, fetch the table rows,
 * and marks the right row with the given actionValue
 * 
 * return the defined action to perform
 */
public class DefaultRowSelector<T> implements RowSelector<TerminalSession, T> {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	public <D extends DrilldownAction> void selectRow(TerminalSession terminalSession, T screenEntity, D drilldownAction,
			int rowNumber) {
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Entry<String, TableDefinition> tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, screenEntity.getClass());

		RowSelectionDefinition rowSelectionDefinition = tableDefinition.getValue().getRowSelectionDefinition();

		String tableFieldName = tableDefinition.getKey();
		List<?> rows = (List<?>)fieldAccessor.getFieldValue(tableFieldName);

		Object row = rows.get(rowNumber);

		fieldAccessor = new SimpleScreenPojoFieldAccessor(row);

		fieldAccessor.setFieldValue(rowSelectionDefinition.getSelectionField(), drilldownAction.getActionValue());

		terminalSession.doAction(drilldownAction, (ScreenEntity)screenEntity);
	}

}
