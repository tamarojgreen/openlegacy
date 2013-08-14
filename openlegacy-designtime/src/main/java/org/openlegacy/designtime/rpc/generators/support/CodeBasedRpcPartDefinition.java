package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.generators.AbstractCodeBasedPartDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeBasedRpcPartDefinition extends AbstractCodeBasedPartDefinition<RpcFieldDefinition, RpcPojoCodeModel> implements RpcPartEntityDefinition {

	private Map<String, RpcFieldDefinition> fields;
	private Map<String, RpcPartEntityDefinition> innerParts = new TreeMap<String, RpcPartEntityDefinition>();
	private int occur;
	private String runtimeName;
	private List<ActionDefinition> actions;

	public CodeBasedRpcPartDefinition(RpcPojoCodeModel codeModel, File packageDir) {
		super(codeModel, packageDir);
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
		return occur;
	}

	public void setOccur(int occur) {
		this.occur = occur;
	}

	public Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions() {
		return innerParts;
	}

	public String getRuntimeName() {
		return runtimeName;
	}

	public void setRuntimeName(String runtimeName) {
		this.runtimeName = runtimeName;
	}

	public List<ActionDefinition> getActions() {
		if (actions == null) {
			actions = RpcCodeBasedDefinitionUtils.getActionsFromCodeModel(getCodeModel(), getPackageDir());
		}
		return actions;
	}
}
