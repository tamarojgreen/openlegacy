package org.openlegacy.terminal.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.support.DefaultRegistryLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

import java.util.Collection;

public class DefaultScreensRegistryLoader extends DefaultRegistryLoader {

	@Override
	protected void fillEntityReferences(EntitiesRegistry<?, ?, ?> entitiesRegistry, EntityDefinition<?> entityDefinition) {
		super.fillEntityReferences(entitiesRegistry, entityDefinition);

		ScreenEntityDefinition screenEntityDefinition = (ScreenEntityDefinition)entityDefinition;
		Collection<ScreenTableDefinition> tables = screenEntityDefinition.getTableDefinitions().values();
		for (ScreenTableDefinition screenTableDefinition : tables) {
			Collection<ScreenFieldDefinition> fields = screenEntityDefinition.getFieldsDefinitions().values();
			for (ScreenFieldDefinition screenFieldDefinition : fields) {
				// add screen fields defined as tableKey to the table keys
				if (screenFieldDefinition.isTableKey()) {
					screenTableDefinition.getKeyFieldNames().add(0, screenFieldDefinition.getName());
				}
			}
		}
	}
}
