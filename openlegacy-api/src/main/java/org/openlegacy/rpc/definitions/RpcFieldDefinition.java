package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.FieldDefinition;

public interface RpcFieldDefinition extends FieldDefinition {

	String getOriginalName();

	Direction getDirection();

	/**
	 * Allows defining floating point length. e.g: 3.2 (2 numbers after the digit)
	 * 
	 * @return
	 */
	double getLength();
}
