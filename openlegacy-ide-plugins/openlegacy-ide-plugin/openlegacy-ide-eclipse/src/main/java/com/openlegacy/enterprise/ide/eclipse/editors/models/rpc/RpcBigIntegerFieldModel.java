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
		fillModel(model);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);

		model.setMinimumValue(this.getMinimumValue());
		model.setMaximumValue(this.getMaximumValue());
		model.setDecimalPlaces(this.getDecimalPlaces());
		model.setPattern(this.getPattern());
		return model;
	}

}
