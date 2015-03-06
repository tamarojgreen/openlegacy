package com.openlegacy.enterprise.ide.eclipse.editors.models.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class RpcIntegerFieldModel extends AbstractRpcNumericFieldModel {

	public RpcIntegerFieldModel(RpcNamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	public RpcIntegerFieldModel(UUID uuid, RpcNamedObject parent) {
		super(parent);
		this.uuid = uuid;
		this.javaTypeName = Messages.getString("type.integer");//$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RpcIntegerFieldModel) || !super.equals(obj)) {
			return false;
		}
		RpcIntegerFieldModel model = (RpcIntegerFieldModel)obj;
		return (this.getMinimumValue() == model.getMinimumValue()) && (this.getMaximumValue() == model.getMaximumValue())
				&& (this.getDecimalPlaces() == model.getDecimalPlaces()) && this.getPattern().equals(model.getPattern());
	}

	@Override
	public RpcIntegerFieldModel clone() {
		// when cloning, innerBranchesCount should not be modified in parent
		int count = ((RpcNamedObject)this.parent).getInnerBranchesCount();

		RpcIntegerFieldModel model = new RpcIntegerFieldModel(this.uuid, (RpcNamedObject)this.parent);
		fillModel(model);
		((RpcNamedObject)this.parent).setInnerBranchesCount(count);

		model.setMinimumValue(this.getMinimumValue());
		model.setMaximumValue(this.getMaximumValue());
		model.setDecimalPlaces(this.getDecimalPlaces());
		model.setPattern(this.getPattern());
		return model;
	}

}
