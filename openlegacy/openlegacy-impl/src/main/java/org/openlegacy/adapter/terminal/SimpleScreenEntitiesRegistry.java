package org.openlegacy.adapter.terminal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.openlegacy.adapter.SimpleHostEntitiesRegistry;
import org.openlegacy.terminal.FieldMapping;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;

/**
 * A simple implementation of a screen entities registry.
 * Holds information collection from @ScreenEntity, @FieldMapping and more
 *
 */
public class SimpleScreenEntitiesRegistry extends SimpleHostEntitiesRegistry
		implements ScreenEntitiesRegistry{

	private final Map<String, ScreenIdentification> screenIdentifications = new HashMap<String, ScreenIdentification>();
	private final Map<Class<?>, Map<String, FieldMapping>> screenFields = new HashMap<Class<?>, Map<String, FieldMapping>>();

	public void addScreenIdentification(
			ScreenIdentification screenIdentification) {
		screenIdentifications.put(screenIdentification.getName(),
				screenIdentification);
	}

	public Class<?> match(TerminalScreen hostScreen) {
		Collection<ScreenIdentification> screensIdentifiersList = screenIdentifications
				.values();
		for (ScreenIdentification screenIdentification : screensIdentifiersList) {
			String screenName = screenIdentification.match(hostScreen);
			if (screenName != null) {
				return getHostEntities().get(screenName);
			}
		}
		return null;
	}

	public void addFieldMapping(Class<?> screenEntity, FieldMapping fieldMapping) {
		if (!getHostEntities().containsValue(screenEntity)) {
			throw (new IllegalArgumentException("Registry doesn''t contain Entity of type:"
					+ screenEntity));
		}
		Map<String, FieldMapping> fields = screenFields.get(screenEntity);
		if (fields == null) {
			fields = new TreeMap<String, FieldMapping>();
			screenFields.put(screenEntity, fields);
		}
		if (!fields.containsKey(fieldMapping.getName())) {
			fields.put(fieldMapping.getName(), fieldMapping);
		}

	}

	public Collection<FieldMapping> getFieldsMappings(Class<?> screenEntity) {
		Map<String, FieldMapping> fieldMappings = screenFields.get(screenEntity);
		if (fieldMappings == null) {
			return Collections.emptyList();
		}
		return fieldMappings.values();
	}
}
