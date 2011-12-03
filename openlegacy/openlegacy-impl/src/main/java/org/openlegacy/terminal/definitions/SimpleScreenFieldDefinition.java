package org.openlegacy.terminal.definitions;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.AbstractFieldDefinition;
import org.openlegacy.FieldType;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public class SimpleScreenFieldDefinition extends AbstractFieldDefinition<ScreenFieldDefinition> implements ScreenFieldDefinition {

	private TerminalPosition position = null;
	private int length;
	private boolean editable;
	private String sampleValue;

	public SimpleScreenFieldDefinition(String name, Class<? extends FieldType> fieldType) {
		super(name, fieldType);
	}

	public TerminalPosition getPosition() {
		return position;
	}

	public void setPosition(TerminalPosition position) {
		this.position = position;
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

	public int compareTo(ScreenFieldDefinition o) {
		return SnapshotUtils.comparePositions(this.getPosition(), o.getPosition());
	}
}
