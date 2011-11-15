package org.openlegacy.terminal.modules.table;

import org.openlegacy.modules.table.drilldown.DrilldownException;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TableUtil {

	@SuppressWarnings("unchecked")
	public static Entry<String, TableDefinition> getSingleTableDefinition(TablesDefinitionProvider tablesDefinitionProvider,
			Class<?> screenEntityClass) {
		Map<String, TableDefinition> tablesDefinitions = tablesDefinitionProvider.getTableDefinitions(screenEntityClass);

		if (tablesDefinitions.size() == 0) {
			throw (new DrilldownException(MessageFormat.format("Specified screen entity:{0} doesn''t contains table definition",
					screenEntityClass)));
		}
		if (tablesDefinitions.size() > 1) {
			throw (new DrilldownException("Drill down currently doesn't support more then one table in a screen"));
		}
		return (Entry<String, TableDefinition>)tablesDefinitions.entrySet().toArray()[0];

	}

	public static List<?> getScreenSingleTable(TablesDefinitionProvider tablesDefinitionProvider, Object screenEntity) {
		Entry<String, TableDefinition> tableDefinition = getSingleTableDefinition(tablesDefinitionProvider,
				screenEntity.getClass());

		ScreenPojoFieldAccessor screenPojoFieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
		List<?> rows = (List<?>)screenPojoFieldAccessor.getFieldValue(tableDefinition.getKey());
		return rows;

	}
}
