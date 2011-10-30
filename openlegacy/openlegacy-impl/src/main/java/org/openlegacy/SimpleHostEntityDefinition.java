package org.openlegacy;

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.exceptions.RegistryException;

import java.lang.reflect.Field;
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

	public String getHostEntityName() {
		return hostEntityName;
	}

	public void setName(String hostEntityName) {
		this.hostEntityName = hostEntityName;
	}

	public Class<?> getHostEntityClass() {
		return hostEntityClass;
	}

	public Map<String, F> getFieldDefinitions() {
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
	public F findFirstFieldDefinitionByType(Class<? extends FieldType> fieldType) {
		Collection<? extends FieldDefinition> fieldValues = fieldDefinitions.values();
		FieldDefinition matchedFieldDefinition = null;
		for (FieldDefinition fieldDefinition : fieldValues) {
			if (fieldDefinition.getType() == fieldType) {
				if (matchedFieldDefinition != null) {
					throw (new RegistryException(MessageFormat.format("Found 2 field of type{0}in class {1}", fieldType,
							getHostEntityClass())));
				}
				matchedFieldDefinition = fieldDefinition;
			}
		}
		return (F)matchedFieldDefinition;
	}

	public Object getFieldValue(Object entityInstance, FieldDefinition fieldDefinition) {
		try {
			Field field = entityInstance.getClass().getDeclaredField(fieldDefinition.getName());
			field.setAccessible(true);
			Object value = field.get(entityInstance);
			return value;
		} catch (Exception e) {
			throw (new OpenLegacyException(e));
		}

	}

}
