package org.openlegacy.terminal.definitions;

import org.openlegacy.AbstractFieldDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.terminal.ScreenPosition;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public class SimpleFieldMappingDefinition extends AbstractFieldDefinition<FieldMappingDefinition> implements FieldMappingDefinition {

	private ScreenPosition screenPosition = null;
	private int length;
	private boolean editable;

	public SimpleFieldMappingDefinition(String name, Class<? extends FieldType> fieldType) {
		super(name, fieldType);
	}

	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}

	public void setScreenPosition(ScreenPosition screenPosition) {
		this.screenPosition = screenPosition;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
