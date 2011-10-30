package org.openlegacy;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 */
public interface FieldDefinition {

	String getName();

	Class<? extends FieldType> getType();
}
