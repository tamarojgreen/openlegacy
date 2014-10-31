package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractRpcNumericFieldModel extends RpcFieldModel {

	// annotation attributes
	private double minimumValue = 0.0;
	private double maximumValue = 0.0;
	private int decimalPlaces = 0;

	public AbstractRpcNumericFieldModel(RpcNamedObject parent) {
		super(parent);
	}

	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		super.init(rpcFieldDefinition);
		if (super.isInitialized() && (rpcFieldDefinition.getFieldTypeDefinition() instanceof RpcNumericFieldTypeDefinition)) {
			RpcNumericFieldTypeDefinition fieldTypeDefinition = (RpcNumericFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
			this.minimumValue = fieldTypeDefinition.getMinimumValue();
			this.maximumValue = fieldTypeDefinition.getMaximumValue();
			this.decimalPlaces = fieldTypeDefinition.getDecimalPlaces();
		}
	}

	public double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(double minimumValue) {
		this.minimumValue = minimumValue;
	}

	public double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;
	}

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

}
