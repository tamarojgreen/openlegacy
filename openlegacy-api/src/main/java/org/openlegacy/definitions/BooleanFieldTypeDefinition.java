package org.openlegacy.definitions;

/**
 * Defines a boolean field type
 * 
 */
public interface BooleanFieldTypeDefinition extends FieldTypeDefinition {

	String getTrueValue();

	String getFalseValue();
}
