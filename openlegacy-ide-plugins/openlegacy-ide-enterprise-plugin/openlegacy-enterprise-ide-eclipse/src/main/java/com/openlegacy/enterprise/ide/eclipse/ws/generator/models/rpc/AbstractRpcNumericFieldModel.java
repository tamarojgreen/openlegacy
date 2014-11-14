package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.rpc.definitions.RpcFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractRpcNumericFieldModel extends RpcFieldModel {

	public AbstractRpcNumericFieldModel(RpcFieldDefinition definition, AbstractNamedModel parent) {
		super(definition, parent);
	}

}
