package org.openlegacy.terminal.modules.table;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.table.drilldown.DrilldownException;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ScrollableTableUtil {

	@SuppressWarnings("unchecked")
	public static Entry<String, TableDefinition> getSingleScrollableTableDefinition(
			TablesDefinitionProvider tablesDefinitionProvider, Class<?> screenEntityClass) {

		Map<String, TableDefinition> tablesDefinitions = tablesDefinitionProvider.getTableDefinitions(screenEntityClass);
		Collection<Entry<String, TableDefinition>> tablesDefinitionEntries = tablesDefinitions.entrySet();

		if (tablesDefinitions.size() == 0) {
			throw (new DrilldownException(MessageFormat.format("Specified screen entity:{0} doesn''t contains table definition",
					screenEntityClass)));
		}
		if (tablesDefinitions.size() > 1) {
			Entry<String, TableDefinition> matchingEntry = null;
			for (Entry<String, TableDefinition> tablesDefinitionEntry : tablesDefinitionEntries) {

				if (tablesDefinitionEntry.getValue().isScrollable()) {
					if (matchingEntry != null) {
						throw (new RegistryException("Only a single scrollable table can be defined in a screen."
								+ screenEntityClass));
					}
					matchingEntry = tablesDefinitionEntry;
				}
			}
			return matchingEntry;
		}
		return (Entry<String, TableDefinition>)tablesDefinitions.entrySet().toArray()[0];

	}

	public static List<?> getSingleScrollableTable(TablesDefinitionProvider tablesDefinitionProvider, Object screenEntity) {
		Entry<String, TableDefinition> tableDefinition = getSingleScrollableTableDefinition(tablesDefinitionProvider,
				screenEntity.getClass());

		ScreenPojoFieldAccessor screenPojoFieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
		List<?> rows = (List<?>)screenPojoFieldAccessor.getFieldValue(tableDefinition.getKey());
		return rows;

	}
}
