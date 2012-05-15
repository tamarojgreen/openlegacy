package org.openlegacy.definitions;

/**
 * Place holder for field type definitions like: Boolean (true/false values), Date field, Auto complete (holds list of values) Any
 * field has a type to simplify field meta-data handling in web layer
 */
public interface FieldTypeDefinition {

	/**
	 * Used by free-marker to determine the field type
	 */
	public String getTypeName();
}
