package org.openlegacy.definitions.support;

import org.openlegacy.FieldType;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;

public abstract class AbstractFieldDefinition<D extends FieldDefinition> implements FieldDefinition {

	private String name;
	private Class<? extends FieldType> type;
	private String displayName;
	private FieldTypeDefinition fieldTypeDefinition;
	private RecordsProvider<? extends Session, Object> recordsProvider;
	private Class<?> sourceEntityClass;
	private boolean collectAll;

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

	public FieldTypeDefinition getFieldTypeDefinition() {
		return fieldTypeDefinition;
	}

	public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefinition) {
		this.fieldTypeDefinition = fieldTypeDefinition;
	}

	public void setRecordsProvider(RecordsProvider<? extends Session, Object> recordsProvider) {
		this.recordsProvider = recordsProvider;
	}

	@SuppressWarnings("unchecked")
	public <S extends Session, T> RecordsProvider<S, T> getRecordsProvider() {
		return (RecordsProvider<S, T>)recordsProvider;
	}

	public Class<?> getSourceEntityClass() {
		return sourceEntityClass;
	}

	public void setSourceScreenEntityClass(Class<?> sourceScreenEntityClass) {
		this.sourceEntityClass = sourceScreenEntityClass;
	}

	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAllRecords(boolean collectAllRecords) {
		this.collectAll = collectAllRecords;
	}
}
