package org.openlegacy.definitions.support;

import org.openlegacy.definitions.NumericFieldTypeDefinition;

public class SimpleNumericFieldTypeDefinition implements NumericFieldTypeDefinition {

	private double minimumValue = Double.MIN_VALUE;
	private double maximumValue = Double.MAX_VALUE;

	public SimpleNumericFieldTypeDefinition() {}

	public SimpleNumericFieldTypeDefinition(double minimumValue, double maximumValue) {
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}

	public double getMinimumValue() {
		return minimumValue;
	}

	public double getMaximumValue() {
		return maximumValue;
	}

	public String getTypeName() {
		return "number";
	}
}
