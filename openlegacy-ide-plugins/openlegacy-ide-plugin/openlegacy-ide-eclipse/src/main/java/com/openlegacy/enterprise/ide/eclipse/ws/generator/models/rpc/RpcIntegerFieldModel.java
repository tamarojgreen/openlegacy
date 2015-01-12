package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.rpc.definitions.RpcFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class RpcIntegerFieldModel extends AbstractRpcNumericFieldModel {

	public RpcIntegerFieldModel(RpcFieldDefinition definition, AbstractNamedModel parent) {
		super(definition, parent);
	}

}
