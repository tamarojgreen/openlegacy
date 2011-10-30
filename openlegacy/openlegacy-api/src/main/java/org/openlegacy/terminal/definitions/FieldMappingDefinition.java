package org.openlegacy.terminal.definitions;

import org.openlegacy.terminal.ScreenPosition;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface FieldMappingDefinition {

	ScreenPosition getScreenPosition();

	int getLength();

	String getName();
}
