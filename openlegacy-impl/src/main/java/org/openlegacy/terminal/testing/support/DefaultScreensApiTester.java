package org.openlegacy.terminal.testing.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.testing.support.AbstractApiTester;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

public class DefaultScreensApiTester extends AbstractApiTester<ScreenEntitiesRegistry, TerminalSession> {

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Override
	protected ScreenEntitiesRegistry getEntitiesRegistry() {
		return entitiesRegistry;
	}

	@Override
	protected void drilldownRecordsActions(TerminalSession session, Object entity) {
		ScreenEntityDefinition entityDefinition = entitiesRegistry.get(entity.getClass());
		Collection<Entry<String, ScreenTableDefinition>> tableDefinitions = entityDefinition.getTableDefinitions().entrySet();

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
		for (Entry<String, ScreenTableDefinition> TableDefinitionEntry : tableDefinitions) {
			List<?> records = (List<?>)fieldAccessor.getFieldValue(TableDefinitionEntry.getKey());
			ScreenTableDefinition tableDefinition = TableDefinitionEntry.getValue();
			List<ActionDefinition> tableActions = tableDefinition.getActions();
			List<String> keyNames = tableDefinition.getKeyFieldNames();
			for (ActionDefinition actionDefinition : tableActions) {
				ScreenPojoFieldAccessor recordAccessor = new SimpleScreenPojoFieldAccessor(records.get(0));
				if (actionDefinition.getTargetEntity() != null) {
					List<Object> keysValues = new ArrayList<Object>();
					for (String keyName : keyNames) {
						keysValues.add(recordAccessor.getFieldValue(keyName));
					}
					try {
						entity = session.getEntity(actionDefinition.getTargetEntity(), keysValues);
					} catch (RuntimeException e) {
						// do nothing
					}
					String entityWithKeys = actionDefinition.getTargetEntity().getSimpleName() + ":"
							+ StringUtils.join(keysValues, "|");
					addNotNullResult("Requested entity with keys is null:" + entityWithKeys, entity);
					addTrueResult("Requested entity wasnt reached:" + entityWithKeys, null,
							actionDefinition.getTargetEntity().isAssignableFrom(entity.getClass()));
				} else {
					String selectionField = tableDefinition.getRowSelectionField();
					addNotNullResult("No selection field found for " + tableDefinition.getTableEntityName(), selectionField);
					recordAccessor.setFieldValue(selectionField,
							((DrilldownAction<?>)actionDefinition.getAction()).getActionValue());
					try {
						entity = session.doAction((TerminalAction)actionDefinition.getAction(), (ScreenEntity)entity);
					} catch (RuntimeException e) {
						// do nothing
					}
					if (!isEnableUnknownEntities()) {
						addNotNullResult(MessageFormat.format("Entity after drill down with action {0} from {1} is empty",
								actionDefinition.getAlias(), tableDefinition.getTableEntityName()), entity);
					}
				}
			}
		}
	}
}
