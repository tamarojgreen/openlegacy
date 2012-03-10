package org.openlegacy.definitions;

import org.openlegacy.FieldType;
import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface FieldDefinition {

	String getName();

	String getDisplayName();

	String getSampleValue();

	Class<? extends FieldType> getType();

	FieldTypeDefinition getFieldTypeDefinition();

	<S extends Session, T> RecordsProvider<S, T> getRecordsProvider();

	boolean isCollectAll();

	Class<?> getSourceEntityClass();
}
