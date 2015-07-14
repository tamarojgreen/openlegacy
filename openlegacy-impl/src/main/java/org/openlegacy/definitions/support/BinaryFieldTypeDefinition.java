package org.openlegacy.definitions.support;

import org.openlegacy.definitions.FieldTypeDefinition;

import java.io.Serializable;

public class BinaryFieldTypeDefinition implements FieldTypeDefinition, Serializable {

	@Override
	public String getTypeName() {
		return "Binary";
	}

}
