package org.openlegacy.definitions.support;

import org.openlegacy.definitions.BooleanFieldTypeDefinition;

public class SimpleBooleanFieldTypeDefinition implements BooleanFieldTypeDefinition {

	private String trueValue;
	private String falseValue;
	private boolean treatNullAsEmpty;

	public SimpleBooleanFieldTypeDefinition(String trueValue, String falseValue, boolean treatNullAsEmpty) {
		this.trueValue = trueValue;
		this.falseValue = falseValue;
		this.treatNullAsEmpty = treatNullAsEmpty;
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

	public boolean isTreatNullAsEmpty() {
		return treatNullAsEmpty;
	}
}
