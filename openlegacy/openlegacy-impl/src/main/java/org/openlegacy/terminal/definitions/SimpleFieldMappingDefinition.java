package org.openlegacy.terminal.definitions;

import org.openlegacy.AbstractFieldDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.terminal.ScreenPosition;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public class SimpleFieldMappingDefinition extends AbstractFieldDefinition<FieldMappingDefinition> implements FieldMappingDefinition {

	private final ScreenPosition screenPosition;
	private final int length;

	public SimpleFieldMappingDefinition(String name, Class<? extends FieldType> fieldType, ScreenPosition screenPosition,
			int length) {
		super(name, fieldType);
		this.screenPosition = screenPosition;
		this.length = length;
	}

	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}

	public int getLength() {
		return length;
	}
}
