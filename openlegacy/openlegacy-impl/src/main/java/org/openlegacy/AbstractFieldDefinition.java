package org.openlegacy;

import org.openlegacy.definitions.FieldDefinition;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition {

	private final String name;
	private Class<? extends FieldType> fieldType;
	private String displayName;

	public AbstractFieldDefinition(String name, Class<? extends FieldType> fieldType) {
		this.name = name;
		this.fieldType = fieldType;
	}

	public String getName() {
		return name;
	}

	public Class<? extends FieldType> getType() {
		return fieldType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
