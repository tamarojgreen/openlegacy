package org.openlegacy.terminal.definitions;

/**
 * Field assign definition defines fields which should be send to the terminal, in the given context (navigation, etc).
 * 
 */
public interface FieldAssignDefinition {

	/**
	 * marker for not sending field content in the given context
	 */
	String NULL = "$XX$";

	String getName();

	String getValue();
}
