package org.openlegacy;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition {

	private final String name;
	private Class<? extends FieldType> fieldType;

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
}
