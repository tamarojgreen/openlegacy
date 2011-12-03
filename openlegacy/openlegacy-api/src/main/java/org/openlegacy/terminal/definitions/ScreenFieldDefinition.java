package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface ScreenFieldDefinition extends FieldDefinition, ScreenPositionContainer, Comparable<ScreenFieldDefinition> {

	ScreenPosition getPosition();

	int getLength();

	boolean isEditable();
}
