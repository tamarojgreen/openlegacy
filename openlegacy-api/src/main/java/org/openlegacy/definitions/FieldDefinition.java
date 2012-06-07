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

	/**
	 * Define an applicative type of the field. e.g: UserField, MenuSelectionField, ErrorField
	 * 
	 * @return
	 */
	Class<? extends FieldType> getType();

	/**
	 * Define the field UI field type: text, password, boolean (check-box), auto complete, date (calendar)
	 * 
	 * @return
	 */
	FieldTypeDefinition getFieldTypeDefinition();

	/**
	 * Holds the field Java type: String, Date, Integer, etc
	 * 
	 * @return
	 */
	public Class<?> getJavaType();

	boolean isKey();
}
