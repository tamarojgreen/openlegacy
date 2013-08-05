package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.designtime.generators.AbstractCodeBasedPartDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.Map;
import java.util.TreeMap;

public class CodeBasedRpcPartDefinition extends AbstractCodeBasedPartDefinition<RpcFieldDefinition, RpcPojoCodeModel> implements RpcPartEntityDefinition {

	private Map<String, RpcFieldDefinition> fields;
	private Map<String, RpcPartEntityDefinition> innerParts = new TreeMap<String, RpcPartEntityDefinition>();;

	public CodeBasedRpcPartDefinition(RpcPojoCodeModel codeModel) {
		super(codeModel);
	}

	@Override
	public Map<String, RpcFieldDefinition> getFieldsDefinitions() {

		if (fields == null) {
			String fieldName = StringUtil.toJavaFieldName(getCodeModel().getEntityName());
			fields = RpcCodeBasedDefinitionUtils.getFieldsFromCodeModel(getCodeModel(), fieldName);
		}
		return fields;
	}

	public int getOrder() {
		return 0;
	}

	public int getOccur() {
		// TODO implement
		return 0;
	}

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions() {
		return innerParts;
	}

}
