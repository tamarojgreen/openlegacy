package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcBigIntegerFieldModel extends RpcIntegerFieldModel {

	public RpcBigIntegerFieldModel(RpcNamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.big.integer");//$NON-NLS-1$
	}

	public RpcBigIntegerFieldModel(UUID uuid, RpcNamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.big.integer");//$NON-NLS-1$
	}

	@Override
	public RpcBigIntegerFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcBigIntegerFieldModel model = new RpcBigIntegerFieldModel(this.uuid, (RpcNamedObject)this.parent);
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

		model.setMinimumValue(this.getMinimumValue());
		model.setMaximumValue(this.getMaximumValue());
		model.setDecimalPlaces(this.getDecimalPlaces());
		return model;
	}

}
