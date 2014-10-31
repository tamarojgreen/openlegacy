package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.rpc.definitions.RpcFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class RpcFieldModel extends AbstractNamedModel {

	private RpcFieldDefinition definition = null;

	public RpcFieldModel(RpcFieldDefinition definition, AbstractNamedModel parent) {
		super(definition.getName(), parent);
		this.definition = definition;
	}

	public RpcFieldDefinition getDefinition() {
		return definition;
	}

}
