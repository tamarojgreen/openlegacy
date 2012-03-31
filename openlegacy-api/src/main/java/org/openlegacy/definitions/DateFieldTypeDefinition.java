package org.openlegacy.definitions;

/**
 * Defines a date field type
 * 
 */
public interface DateFieldTypeDefinition extends FieldTypeDefinition {

	Integer getYearColumn();

	Integer getMonthColumn();

	Integer getDayColumn();
}
