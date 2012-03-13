package org.openlegacy.definitions.support;

import org.openlegacy.definitions.BooleanFieldTypeDefinition;

public class SimpleBooleanFieldTypeDefinition implements BooleanFieldTypeDefinition {

	private String trueValue;
	private String falseValue;

	public SimpleBooleanFieldTypeDefinition(String trueValue, String falseValue) {
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public String getTypeName() {
		return "boolean";
	}

}
