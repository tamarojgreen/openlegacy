package org.openlegacy.terminal.support.binders;

import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenTableReferenceDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

public class MultyScreenTableBindUtil {

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@SuppressWarnings("rawtypes")
	public Object bindCollectTable(TerminalSession session, Object entity) {
		Entry<String, ScreenTableDefinition> definitionEntry = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, entity.getClass());
		if (definitionEntry == null) {
			return entity;
		}
		ScreenTableDefinition tableDefinition = definitionEntry.getValue();

		String tableFieldName = tableDefinition.getTableEntityName() + "s";
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
		if (definitionEntry.getValue().getScreensCount() > 1) {
			List<?> rows = session.getModule(Table.class).collect(entity.getClass(), tableDefinition.getTableClass(),
					tableDefinition.getScreensCount());
			if (fieldAccessor.isExists(tableFieldName)) {
				fieldAccessor.setFieldValue(tableFieldName, rows);
			}
		}

		// collect related screen tables
		List<ScreenTableReferenceDefinition> tableReferenceDefinitions = tableDefinition.getTableReferenceDefinitions();
		for (ScreenTableReferenceDefinition tableReferenceDefinition : tableReferenceDefinitions) {
			if (tableReferenceDefinition.getRelatedScreen() != null) {
				Object relatedEntity = session.getEntity(tableReferenceDefinition.getRelatedScreen());
				List mainEntityRows = (List)fieldAccessor.getFieldValue(tableFieldName);

				Entry<String, ScreenTableDefinition> relatedScreenTableDefinitionEntry = ScrollableTableUtil.getSingleScrollableTableDefinition(
						tablesDefinitionProvider, tableReferenceDefinition.getRelatedScreen());
				ScreenPojoFieldAccessor relatedEntityFieldAccessor = new SimpleScreenPojoFieldAccessor(relatedEntity);

				if (relatedScreenTableDefinitionEntry != null) {
					List relatedRows = (List)relatedEntityFieldAccessor.getFieldValue(relatedScreenTableDefinitionEntry.getKey());
					if (mainEntityRows != null) {
						int index = 0;
						for (Object mainRow : mainEntityRows) {
							ScreenPojoFieldAccessor rowAccessor = new SimpleScreenPojoFieldAccessor(mainRow);
							// attach each row from related entity to main row
							rowAccessor.setFieldValue(tableReferenceDefinition.getFieldName(), relatedRows.get(index++));
						}
					}
				}
				// navigate back to main screen
				session.getEntity(entity.getClass());
			}
		}
		return entity;

	}
}
