package org.openlegacy.definitions.support;

import org.openlegacy.definitions.BooleanFieldDefinition;

public class SimpleBooleanFieldDefinition implements BooleanFieldDefinition {

	private String trueValue;
	private String falseValue;

	public SimpleBooleanFieldDefinition(String trueValue, String falseValue) {
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
