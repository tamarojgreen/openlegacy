package org.openlegacy;

public interface EntityFieldAccessor {

	boolean isExists(String fieldName);

	boolean isWritable(String fieldName);

	void setFieldValue(String fieldName, Object value);

	Class<?> getFieldType(String fieldName);

	Object getFieldValue(String fieldName);

}