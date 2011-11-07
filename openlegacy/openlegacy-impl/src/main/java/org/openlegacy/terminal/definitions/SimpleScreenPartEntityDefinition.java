package org.openlegacy.terminal.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleScreenPartEntityDefinition implements ScreenPartEntityDefinition {

	private Class<?> partClass;

	private final Map<String, FieldMappingDefinition> fieldDefinitions = new LinkedHashMap<String, FieldMappingDefinition>();

	public SimpleScreenPartEntityDefinition(Class<?> partClass) {
		this.partClass = partClass;
	}

	public Class<?> getPartClass() {
		return partClass;
	}

	public Map<String, FieldMappingDefinition> getFieldsDefinitions() {
		return fieldDefinitions;
	}

}
