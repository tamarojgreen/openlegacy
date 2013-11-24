package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Fetch FieldInformation from Cobol array of string or number declaration.
 * 
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcListFieldTypeDefinition;

import java.util.List;

public class CobolListInformation implements FieldInformation {

	private int count;
	private FieldInformation fieldInformation;

	public CobolListInformation(FieldInformation fieldInformation, int count) {
		this.count = count;
		this.fieldInformation = fieldInformation;

	}

	public int getLength() {
		return fieldInformation.getLength();
	}

	public Class<?> getJavaType() {

		return List.class;
	}

	public FieldTypeDefinition getType() {

		return new SimpleRpcListFieldTypeDefinition(fieldInformation.getLength(), count, fieldInformation.getType(),
				fieldInformation.getJavaType());
	}

}
