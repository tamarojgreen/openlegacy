package org.openlegacy.terminal.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleScreenPartEntityDefinition implements ScreenPartEntityDefinition {

	private Class<?> partClass;

	private final Map<String, ScreenFieldDefinition> fieldDefinitions = new LinkedHashMap<String, ScreenFieldDefinition>();

	public SimpleScreenPartEntityDefinition(Class<?> partClass) {
		this.partClass = partClass;
	}

	public Class<?> getPartClass() {
		return partClass;
	}

	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		return fieldDefinitions;
	}

}
