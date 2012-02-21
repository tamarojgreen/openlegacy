package org.openlegacy.terminal.definitions;

/**
 * Field assign definition defines fields which should be send to the terminal, in the given context (navigation, etc).
 * 
 */
public interface FieldAssignDefinition {

	String getName();

	String getValue();
}
