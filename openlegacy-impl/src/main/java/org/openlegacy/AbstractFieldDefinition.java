package org.openlegacy;

import org.openlegacy.definitions.FieldDefinition;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition {

	private String name;
	private Class<? extends FieldType> type;
	private String displayName;

	public AbstractFieldDefinition(String name, Class<? extends FieldType> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends FieldType> getType() {
		return type;
	}

	public void setType(Class<? extends FieldType> type) {
		this.type = type;
	}

	public String getTypeName() {
		if (type == null) {
			return null;
		}
		return type.getSimpleName();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
