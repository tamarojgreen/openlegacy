package org.openlegacy.definitions;


/**
 * Defines a boolean screen field  
 *
 */
public interface BooleanFieldDefinition extends FieldTypeDefinition{

	String getTrueValue();
	
	String getFalseValue();
}
