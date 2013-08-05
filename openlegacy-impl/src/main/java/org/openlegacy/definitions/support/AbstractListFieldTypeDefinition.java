package org.openlegacy.definitions.support;

/**
 * An abstract implementation of ListFieldTypeDefinition
 * 
 */

public class AbstractListFieldTypeDefinition {

	protected int fieldLength;
	protected int count;

	public AbstractListFieldTypeDefinition() {
		this.fieldLength = 0;
		this.count = 1;
	}

	public AbstractListFieldTypeDefinition(int fieldLength, int count) {
		this.fieldLength = fieldLength;
		this.count = count;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public int getCount() {
		return count;
	}

	public String getTypeName() {
		return "list";
	}

}