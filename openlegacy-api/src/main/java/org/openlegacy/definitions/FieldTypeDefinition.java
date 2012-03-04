package org.openlegacy.definitions;

/**
 * Please holder for field type definitions like: BooleanField (true/false values), etc
 */
public interface FieldTypeDefinition {

	/**
	 * Used by free-marker to determine the field type
	 */
	public String getTypeName();
}
