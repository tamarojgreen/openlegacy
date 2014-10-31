package com.openlegacy.enterprise.ide.eclipse.editors.support.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;

import java.util.Comparator;

/**
 * @author Ivan Bort
 * 
 */
public class RpcFieldModelNameComparator implements Comparator<RpcFieldModel> {

	public static RpcFieldModelNameComparator INSTANCE = new RpcFieldModelNameComparator();

	private RpcFieldModelNameComparator() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(RpcFieldModel o1, RpcFieldModel o2) {
		return 0;
	}

}
