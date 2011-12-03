package org.openlegacy.terminal.definitions;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.AbstractFieldDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.terminal.ScreenPosition;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public class SimpleFieldMappingDefinition extends AbstractFieldDefinition<ScreenFieldDefinition> implements ScreenFieldDefinition {

	private ScreenPosition screenPosition = null;
	private int length;
	private boolean editable;
	private String sampleValue;

	public SimpleFieldMappingDefinition(String name, Class<? extends FieldType> fieldType) {
		super(name, fieldType);
	}

	public ScreenPosition getPosition() {
		return screenPosition;
	}

	public void setPosition(ScreenPosition screenPosition) {
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

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
