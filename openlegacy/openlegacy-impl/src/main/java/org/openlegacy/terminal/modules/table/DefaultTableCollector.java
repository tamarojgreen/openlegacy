package org.openlegacy.terminal.modules.table;

import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.table.ScreenTableCollector;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

public class DefaultTableCollector<T> implements ScreenTableCollector<T> {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@SuppressWarnings("unchecked")
	public List<T> collectAll(TerminalSession terminalSession, Class<?> screenEntityClass, Class<T> rowClass) {

		ScreenEntityDefinition screenEntityDefintion = screenEntitiesRegistry.get(screenEntityClass);
		Map<String, TableDefinition> tableDefinitionsMap = screenEntityDefintion.getTableDefinitions();
		Set<Entry<String, TableDefinition>> tableDefinitionEntries = tableDefinitionsMap.entrySet();

		Entry<String, TableDefinition> matchingDefinitionEntry = null;

		for (Entry<String, TableDefinition> tableDefinitionEntry : tableDefinitionEntries) {
			if (tableDefinitionEntry.getValue().getTableClass() == rowClass) {
				matchingDefinitionEntry = tableDefinitionEntry;
				break;
			}
		}

		if (matchingDefinitionEntry == null) {
			throw (new IllegalArgumentException(MessageFormat.format("Could not find table of type {0} in {1}", rowClass,
					screenEntityDefintion)));
		}

		TableDefinition matchingTableDefinition = matchingDefinitionEntry.getValue();
		int rowsCount = matchingTableDefinition.getEndRow() - matchingTableDefinition.getStartRow() + 1;

		List<T> allRows = new ArrayList<T>();

		List<T> rows = null;
		boolean cont = true;

		Object screenEntity = terminalSession.getEntity(screenEntityClass);
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		while (cont) {

			rows = (List<T>)fieldAccessor.getFieldValue(matchingDefinitionEntry.getKey());
			allRows.addAll(rows);

			// more rows exists
			if (rows.size() < rowsCount) {
				cont = false;
			} else {
				screenEntity = terminalSession.doAction(matchingTableDefinition.getNextScreenAction(), null);
				fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			}
		}

		return allRows;
	}

}
