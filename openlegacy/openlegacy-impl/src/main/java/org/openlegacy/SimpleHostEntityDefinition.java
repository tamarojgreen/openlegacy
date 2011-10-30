package org.openlegacy;

import org.openlegacy.exceptions.RegistryException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class SimpleHostEntityDefinition<F extends FieldDefinition> implements HostEntityDefinition<F> {

	private String hostEntityName;
	private Class<?> hostEntityClass;

	private Class<? extends HostEntityType> hostEntityType;

	private final Map<String, F> fieldDefinitions = new HashMap<String, F>();

	public SimpleHostEntityDefinition(String hostEntityName, Class<?> screenEntityClass) {
		this.hostEntityName = hostEntityName;
		this.hostEntityClass = screenEntityClass;
	}

	public String getEntityName() {
		return hostEntityName;
	}

	public void setName(String hostEntityName) {
		this.hostEntityName = hostEntityName;
	}

	public Class<?> getEntityClass() {
		return hostEntityClass;
	}

	public Map<String, F> getFieldsDefinitions() {
		return fieldDefinitions;
	}

	public void setHostEntityClass(Class<?> hostEntityClass) {
		this.hostEntityClass = hostEntityClass;
	}

	public Class<? extends HostEntityType> getType() {
		return hostEntityType;
	}

	public void setType(Class<? extends HostEntityType> hostEntityType) {
		this.hostEntityType = hostEntityType;
	}

	@SuppressWarnings("unchecked")
	public F getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		Collection<? extends FieldDefinition> fieldValues = fieldDefinitions.values();
		FieldDefinition matchedFieldDefinition = null;
		for (FieldDefinition fieldDefinition : fieldValues) {
			if (fieldDefinition.getType() == fieldType) {
				if (matchedFieldDefinition != null) {
					throw (new RegistryException(MessageFormat.format("Found 2 field of type{0}in class {1}", fieldType,
							getEntityClass())));
				}
				matchedFieldDefinition = fieldDefinition;
			}
		}
		return (F)matchedFieldDefinition;
	}

}
