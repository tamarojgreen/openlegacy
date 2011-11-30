package org.openlegacy.definitions;

import org.openlegacy.FieldType;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface FieldDefinition {

	String getName();

	String getDisplayName();

	String getSampleValue();

	Class<? extends FieldType> getType();
}
