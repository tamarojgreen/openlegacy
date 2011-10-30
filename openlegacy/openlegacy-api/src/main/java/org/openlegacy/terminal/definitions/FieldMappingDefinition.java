package org.openlegacy.terminal.definitions;

import org.openlegacy.FieldDefinition;
import org.openlegacy.terminal.ScreenPosition;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface FieldMappingDefinition extends FieldDefinition {

	ScreenPosition getScreenPosition();

	int getLength();
}
