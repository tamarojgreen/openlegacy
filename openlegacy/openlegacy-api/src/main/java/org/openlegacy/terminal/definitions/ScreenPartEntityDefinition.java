package org.openlegacy.terminal.definitions;

import java.util.Map;

public interface ScreenPartEntityDefinition {

	Class<?> getPartClass();

	Map<String, FieldMappingDefinition> getFieldsDefinitions();
}
