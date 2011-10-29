package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.ScreenEntityDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * A ChildScreensDefinitionProvider based on open legacy @ChildScreenEntity annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedChildScreensDefinitionProvider implements ChildScreensDefinitionProvider {

	@Autowired
	private SimpleScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<ChildScreenDefinition> getChildScreenDefinitions(Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getChildScreenDefinitions().values();
	}

	public ChildScreenDefinition getChildScreenDefinitions(Class<?> screenEntity, String fieldName) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getChildScreenDefinitions().get(fieldName);
	}
}
