package org.openlegacy.providers.wsrpc;

import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;

public class WsRpcConnectionFactory implements LiveRpcConnectionFactory {

	@Override
	public RpcConnection getConnection() {
		return new WsRpcConnection();
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

}
