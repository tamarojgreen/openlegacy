package org.openlegacy;

public interface HostEntityFieldAccessor {

	public abstract boolean isExists(String fieldName);

	public abstract boolean isEditable(String fieldName);

	public abstract void setFieldValue(String fieldName, Object value);

	public abstract Class<?> getFieldType(String fieldName);

	public abstract Object getFieldValue(String fieldName);

}