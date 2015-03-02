package com.openlegacy.enterprise.ide.eclipse.editors.support.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;

import java.util.Comparator;

public class RpcOrderFieldComparator implements Comparator<RpcFieldModel> {

	@Override
	public int compare(RpcFieldModel arg0, RpcFieldModel arg1) {
		return arg0.getOrder() - arg1.getOrder();
	}

}
