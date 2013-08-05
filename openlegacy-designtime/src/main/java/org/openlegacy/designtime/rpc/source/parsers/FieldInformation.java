package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.definitions.FieldTypeDefinition;

public interface FieldInformation {

	int getLength();

	Class<?> getJavaType();

	FieldTypeDefinition getType();
}