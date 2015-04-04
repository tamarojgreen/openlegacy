package org.openlegacy.terminal.support.binders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.FieldWithValuesTypeDefinition;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenTableReferenceDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

public class MultyScreenTableBindUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private final static Log logger = LogFactory.getLog(MultyScreenTableBindUtil.class);

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

	public Map<Object, Object> getRecords(TerminalSession terminalSession, String searchText, String entityName,
			String propertyName) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(entityName);
		ScreenFieldDefinition fieldDefinition = screenEntityDefinition.getFieldsDefinitions().get(propertyName);
		if (fieldDefinition == null || !(fieldDefinition.getFieldTypeDefinition() instanceof FieldWithValuesTypeDefinition)) {
			return null;
		}
		FieldWithValuesTypeDefinition fieldTypeDefinition = (FieldWithValuesTypeDefinition)fieldDefinition.getFieldTypeDefinition();
		RecordsProvider<Session, Object> recordsProvider = fieldTypeDefinition.getRecordsProvider();

		ScreenTableDefinition tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(tablesDefinitionProvider,
				fieldTypeDefinition.getSourceEntityClass()).getValue();

		String searchField = fieldTypeDefinition.getSearchField();
		if (searchField != null) {
			Class<?> sourceEntityClass = fieldTypeDefinition.getSourceEntityClass();
			if (searchText == null) {
				logger.warn(MessageFormat.format("Search field {0} for entity {1} has not recieved search paramters",
						searchField, sourceEntityClass.getSimpleName()));
			} else {
				ScreenEntity sourceEntity = (ScreenEntity)terminalSession.getEntity(sourceEntityClass);
				ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(sourceEntity);
				fieldAccessor.setFieldValue(searchField, searchText);
				terminalSession.doAction(TerminalActions.ENTER(), sourceEntity);
			}
		}
		@SuppressWarnings("unchecked")
		Map<Object, Object> records = recordsProvider.getRecords(terminalSession, fieldTypeDefinition.getSourceEntityClass(),
				(Class<Object>)tableDefinition.getTableClass(), fieldTypeDefinition.isCollectAll(), null);
		return records;
	}

}
