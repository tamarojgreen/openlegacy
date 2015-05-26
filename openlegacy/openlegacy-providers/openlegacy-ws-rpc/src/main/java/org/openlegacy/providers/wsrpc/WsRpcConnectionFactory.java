package org.openlegacy.providers.wsrpc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;

public class WsRpcConnectionFactory implements RpcConnectionFactory {

	@Override
	public RpcConnection getConnection() {
		return new WsRpcConnection();
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

}
