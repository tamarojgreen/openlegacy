package org.openlegacy.terminal.definitions;

import java.util.Map;

/**
 * A screen part definition defines a repeatable class with mappings which can belongs to a 1 or more screen entities. Typically
 * loaded from @ScreenPart annotation
 * 
 */
public interface ScreenPartEntityDefinition {

	Class<?> getPartClass();

	Map<String, ScreenFieldDefinition> getFieldsDefinitions();

	String getPartName();
}
