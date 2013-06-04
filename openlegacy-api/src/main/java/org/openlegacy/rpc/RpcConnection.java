package org.openlegacy.rpc;

import org.openlegacy.ApplicationConnection;

public interface RpcConnection extends ApplicationConnection<RpcSnapshot, RpcInvokeAction> {

	Object getDelegate();

	boolean isConnected();

	void disconnect();

	RpcResult invoke(RpcInvokeAction rpcInvokeAction);
}
