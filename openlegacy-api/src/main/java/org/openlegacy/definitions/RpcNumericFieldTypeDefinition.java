package org.openlegacy.definitions;

/**
 * Defines a rpc numeric field type registry information stored within {@link NumericFieldDefinition}.
 */

public interface RpcNumericFieldTypeDefinition extends NumericFieldTypeDefinition {

	int getDecimalPlaces();
}
