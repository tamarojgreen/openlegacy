package org.openlegacy.cache.tests.stuff;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.mock.MockRpcConnection;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.util.List;

public class CrudMockRpcConnection extends MockRpcConnection {

	public CrudMockRpcConnection(List<RpcSnapshot> rpcSnapshots) {
		super(rpcSnapshots);
	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		SimpleRpcResult rpcResult = (SimpleRpcResult) super.invoke(rpcInvokeAction);
		if (StringUtils.equals(rpcInvokeAction.getAction(), "UPDATE")) {
			rpcResult.setRpcFields(rpcInvokeAction.getFields());
		}
		return rpcResult;
	}

}
