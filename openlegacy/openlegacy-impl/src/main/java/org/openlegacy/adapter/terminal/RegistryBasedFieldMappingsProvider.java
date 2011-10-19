package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.FieldMapping;
import org.openlegacy.terminal.FieldMappingsProvider;
import org.openlegacy.terminal.TerminalScreen;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * A FieldMappingsProvider based on open legacy @FieldMapping annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedFieldMappingsProvider implements FieldMappingsProvider {

	@Autowired
	private SimpleScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<FieldMapping> getFieldsMappings(TerminalScreen terminalScreen, Class<?> screenEntity) {
		return screenEntitiesRegistry.getFieldsMappings(screenEntity);
	}
}
