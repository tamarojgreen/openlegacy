package org.openlegacy.rpc.utils;

import org.openlegacy.rpc.RpcPojoFieldAccessor;

public class SimpleHierarchyRpcPojoFieldAccessor implements HierarchyRpcPojoFieldAccessor {

	private RpcPojoFieldAccessor topLevelAccessor;
	protected Object entity;

	public SimpleHierarchyRpcPojoFieldAccessor(Object entity) {
		this.entity = entity;
		topLevelAccessor = new SimpleRpcPojoFieldAccessor(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.utils.HierarchyRpcPojoFieldAccessor#getPartAccessor(java.lang.String)
	 */
	@Override
	public RpcPojoFieldAccessor getPartAccessor(String fullPath) {
		RpcPojoFieldAccessor result = topLevelAccessor;
		while (fullPath.indexOf('.') > 0) {
			String partName = org.springframework.util.StringUtils.uncapitalize(fullPath.substring(0, fullPath.indexOf('.')));
			Object part = result.getFieldValue(partName);
			result = new SimpleRpcPojoFieldAccessor(part);
			fullPath = fullPath.substring(partName.length() + 1);
		}
		return result;
	}
}
