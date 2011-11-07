package org.openlegacy.loaders.support;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ScreenPartFieldsLoader implements FieldLoader {

	@SuppressWarnings("rawtypes")
	public boolean match(HostEntitiesRegistry entitiesRegistry, Field field) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		return (screenEntitiesRegistry.getPart(field.getType()) != null);
	}

	@SuppressWarnings("rawtypes")
	public void load(HostEntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenPartEntityDefinition partDefinition = screenEntitiesRegistry.getPart(field.getType());
		if (partDefinition != null) {
			screenEntitiesRegistry.get(containingClass).getPartsDefinitions().put(field.getName(), partDefinition);
		}

	}
}
