package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Fetch FieldInformation from Cobol text variable declaration.
 * 
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;

public class CobolTextInformation implements FieldInformation {

	private int length;

	public CobolTextInformation(String flatPicture) {
		length = flatPicture.length();
	}

	public int getLength() {

		return length;
	}

	public Class<?> getJavaType() {

		return String.class;
	}

	public FieldTypeDefinition getType() {
		return new SimpleTextFieldTypeDefinition();
	}
}
