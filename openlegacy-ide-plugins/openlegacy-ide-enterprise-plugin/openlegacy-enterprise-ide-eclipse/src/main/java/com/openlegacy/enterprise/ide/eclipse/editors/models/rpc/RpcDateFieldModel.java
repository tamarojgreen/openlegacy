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

package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.rpc.RpcDateField;
import org.openlegacy.definitions.DateFieldTypeDefinition;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcDateFieldModel extends RpcFieldModel {

	// annotation attributes
	private String pattern;

	public RpcDateFieldModel(RpcNamedObject parent) {
		super(RpcDateField.class.getSimpleName(), parent);
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	public RpcDateFieldModel(UUID uuid, RpcNamedObject parent) {
		super(RpcDateField.class.getSimpleName(), parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.date");//$NON-NLS-1$
	}

	@Override
	public void init(RpcFieldDefinition rpcFieldDefinition) {
		super.init(rpcFieldDefinition);
		if (super.isInitialized() && (rpcFieldDefinition.getFieldTypeDefinition() instanceof DateFieldTypeDefinition)) {
			DateFieldTypeDefinition definition = (DateFieldTypeDefinition)rpcFieldDefinition.getFieldTypeDefinition();
			this.pattern = definition.getPattern();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RpcDateFieldModel) || !super.equals(obj)) {
			return false;
		}
		RpcDateFieldModel model = (RpcDateFieldModel)obj;
		return StringUtils.equals(pattern, model.getPattern());
	}

	@Override
	public RpcDateFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcDateFieldModel model = new RpcDateFieldModel(this.uuid, (RpcNamedObject)this.parent);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);
		model.setTreeLevel(this.getTreeLevel());
		model.setTreeBranch(this.getTreeBranch());
		model.setModelName(this.modelName);
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setFieldTypeName(this.getFieldTypeName());
		model.setOriginalName(this.getOriginalName());
		model.setKey(this.isKey());
		model.setDirection(this.getDirection());
		model.setLength(this.getLength());
		model.setFieldType(this.getFieldType());
		model.setDisplayName(this.getDisplayName());
		model.setSampleValue(this.getSampleValue());
		model.setHelpText(this.getHelpText());
		model.setEditable(this.isEditable());
		model.setDefaultValue(this.getDefaultValue());

		model.setPattern(pattern);
		return model;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
