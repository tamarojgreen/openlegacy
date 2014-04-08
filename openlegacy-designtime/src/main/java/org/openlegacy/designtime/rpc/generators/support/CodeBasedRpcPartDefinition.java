/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.generators.AbstractCodeBasedPartDefinition;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.RpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeBasedRpcPartDefinition extends AbstractCodeBasedPartDefinition<RpcFieldDefinition, RpcPojoCodeModel> implements RpcPartEntityDefinition {

	private Map<String, RpcFieldDefinition> fields;
	private Map<String, RpcPartEntityDefinition> innerParts = new TreeMap<String, RpcPartEntityDefinition>();
	private int count;
	private String runtimeName;
	private List<ActionDefinition> actions;

	private List<RpcFieldDefinition> keys;

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public List<RpcFieldDefinition> getKeys() {
		if (keys != null) {
			return keys;
		}

		keys = new ArrayList<RpcFieldDefinition>();

		for (RpcFieldDefinition field : getFieldsDefinitions().values()) {
			if (field.isKey()) {
				keys.add(field);
			}
		}
		return keys;
	}

	public void setKeys(List<RpcFieldDefinition> keys) {
		this.keys = keys;
	}

	public String getLegacyContainerName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHelpText() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isVirtual() {
		// TODO Auto-generated method stub
		return null;
	}
}
