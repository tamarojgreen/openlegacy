package org.openlegacy.definitions.support;

import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;

public class SimpleRpcNumericFieldTypeDefinition extends SimpleNumericFieldTypeDefinition implements RpcNumericFieldTypeDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int decimalPlaces = 0;

	public SimpleRpcNumericFieldTypeDefinition() {
		super();
	}

	public SimpleRpcNumericFieldTypeDefinition(double minimumValue, double maximumValue, int decimalPlaces) {
		super(minimumValue, maximumValue);
		this.decimalPlaces = decimalPlaces;
	}

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleRpcNumericFieldTypeDefinition) {
			SimpleRpcNumericFieldTypeDefinition convertedObject = (SimpleRpcNumericFieldTypeDefinition)object;

			if (super.equals(convertedObject) == true && convertedObject.decimalPlaces == decimalPlaces) {
				return true;
			}
		}
		return false;

	}
}
