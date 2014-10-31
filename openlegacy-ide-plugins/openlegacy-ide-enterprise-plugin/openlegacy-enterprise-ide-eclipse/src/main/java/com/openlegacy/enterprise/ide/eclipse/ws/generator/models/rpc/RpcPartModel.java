package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.rpc.RpcEntityUtils;

import org.openlegacy.designtime.rpc.generators.support.CodeBasedRpcPartDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public class RpcPartModel extends AbstractPartModel {

	private CodeBasedRpcPartDefinition definition = null;

	public RpcPartModel(CodeBasedRpcPartDefinition definition, AbstractNamedModel parent) {
		super(definition.getPartName(), parent);
		this.definition = definition;

		// populate children
		Map<String, RpcFieldDefinition> fieldsDefinitions = definition.getFieldsDefinitions();
		if (fieldsDefinitions != null && !fieldsDefinitions.isEmpty()) {
			Collection<RpcFieldDefinition> values = fieldsDefinitions.values();
			children.addAll(RpcEntityUtils.getFields(new ArrayList<RpcFieldDefinition>(values), this));
		}
	}

	public CodeBasedRpcPartDefinition getDefinition() {
		return definition;
	}

}
