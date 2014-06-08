package org.openlegacy.rpc.utils;

import org.openlegacy.rpc.RpcPojoFieldAccessor;

public interface HierarchyRpcPojoFieldAccessor {

	RpcPojoFieldAccessor getPartAccessor(String fullPath);

}