package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * A ChildScreensDefinitionProvider based on open legacy @ChildScreenEntity annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedChildScreensDefinitionProvider implements ChildScreensDefinitionProvider {

	@Autowired
	private DefaultScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<ChildScreenDefinition> getChildScreenDefinitions(Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getChildScreenDefinitions().values();
	}

	public ChildScreenDefinition getChildScreenDefinitions(Class<?> screenEntity, String fieldName) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getChildScreenDefinitions().get(fieldName);
	}
}
