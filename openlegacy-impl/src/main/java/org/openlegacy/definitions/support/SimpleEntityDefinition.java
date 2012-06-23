package org.openlegacy.definitions.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.terminal.ScreenEntityType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class SimpleEntityDefinition<F extends FieldDefinition> implements EntityDefinition<F> {

	private String entityName;
	private Class<?> entityClass;

	private Class<? extends EntityType> entityType;

	// LinkedHashMap preserve insert order
	private final Map<String, F> fieldDefinitions = new TreeMap<String, F>();
	private String displayName;
	private ArrayList<F> keyFields;

	private List<EntityDefinition<?>> childEntitiesDefinitions = new ArrayList<EntityDefinition<?>>();

	public SimpleEntityDefinition(String entityName, Class<?> screenEntityClass) {
		this.entityName = entityName;
		this.entityClass = screenEntityClass;
	}

	public String getEntityName() {
		return entityName;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getEntityClassName() {
		return getEntityClass().getSimpleName();
	}

	public Map<String, F> getFieldsDefinitions() {
		return fieldDefinitions;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<? extends EntityType> getType() {
		return entityType;
	}

	public String getTypeName() {
		if (getType() == null) {
			return ScreenEntityType.General.class.getSimpleName();
		}
		return getType().getSimpleName();
	}

	public void setType(Class<? extends EntityType> entityType) {
		this.entityType = entityType;
	}

	@SuppressWarnings("unchecked")
	public F getFirstFieldDefinition(Class<? extends FieldType> fieldType) {
		Collection<? extends FieldDefinition> fieldValues = fieldDefinitions.values();
		FieldDefinition matchedFieldDefinition = null;
		for (FieldDefinition fieldDefinition : fieldValues) {
			if (fieldDefinition.getType() == fieldType) {
				if (matchedFieldDefinition != null) {
					throw (new RegistryException(MessageFormat.format("Found 2 field of type {0} in class {1}", fieldType,
							getEntityClass())));
				}
				matchedFieldDefinition = fieldDefinition;
			}
		}
		return (F)matchedFieldDefinition;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;

	}

	public List<? extends FieldDefinition> getKeys() {
		if (keyFields == null) {
			Collection<F> allFields = fieldDefinitions.values();
			keyFields = new ArrayList<F>();
			for (F field : allFields) {
				if (field.isKey()) {
					keyFields.add(field);
				}
			}
		}
		return keyFields;
	}

	public List<EntityDefinition<?>> getChildEntitiesDefinitions() {
		return childEntitiesDefinitions;
	}

}
