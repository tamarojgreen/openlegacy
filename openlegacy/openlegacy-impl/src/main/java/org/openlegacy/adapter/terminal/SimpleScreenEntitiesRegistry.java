package org.openlegacy.adapter.terminal;

import org.openlegacy.adapter.SimpleHostEntitiesRegistry;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple implementation of a screen entities registry. Holds information collection from @ScreenEntity, @FieldMapping and more
 * 
 */
public class SimpleScreenEntitiesRegistry extends SimpleHostEntitiesRegistry implements ScreenEntitiesRegistry {

	private final Map<String, ScreenIdentification> screenIdentifications = new HashMap<String, ScreenIdentification>();
	private final Map<Class<?>, Map<String, FieldMappingDefinition>> screenFields = new HashMap<Class<?>, Map<String, FieldMappingDefinition>>();

	public void addScreenIdentification(ScreenIdentification screenIdentification) {
		screenIdentifications.put(screenIdentification.getName(), screenIdentification);
	}

	public Class<?> match(TerminalScreen hostScreen) {
		Collection<ScreenIdentification> screensIdentifiersList = screenIdentifications.values();
		for (ScreenIdentification screenIdentification : screensIdentifiersList) {
			String screenName = screenIdentification.match(hostScreen);
			if (screenName != null) {
				return getHostEntities().get(screenName);
			}
		}
		return null;
	}

	public void addFieldMappingDefinition(Class<?> screenEntity, FieldMappingDefinition fieldMappingDefinition) {
		if (!getHostEntities().containsValue(screenEntity)) {
			throw (new IllegalArgumentException("Registry doesn''t contain Entity of type:" + screenEntity));
		}
		Map<String, FieldMappingDefinition> fields = screenFields.get(screenEntity);
		if (fields == null) {
			fields = new TreeMap<String, FieldMappingDefinition>();
			screenFields.put(screenEntity, fields);
		}
		if (!fields.containsKey(fieldMappingDefinition.getName())) {
			fields.put(fieldMappingDefinition.getName(), fieldMappingDefinition);
		}

	}

	public Collection<FieldMappingDefinition> getFieldsMappings(Class<?> screenEntity) {
		Map<String, FieldMappingDefinition> fieldMappings = screenFields.get(screenEntity);
		if (fieldMappings == null) {
			return Collections.emptyList();
		}
		return fieldMappings.values();
	}
}
