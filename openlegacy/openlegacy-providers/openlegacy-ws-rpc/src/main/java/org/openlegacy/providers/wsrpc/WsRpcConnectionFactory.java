package org.openlegacy.providers.wsrpc;

import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;

public class WsRpcConnectionFactory implements LiveRpcConnectionFactory {

	private String baseURL;

	public WsRpcConnectionFactory() {
		this("");
	}

	public WsRpcConnectionFactory(String baseURL) {
		this.baseURL = baseURL;
	}

	@Override
	public RpcConnection getConnection() {
		WsRpcConnection connection = new WsRpcConnection();
		connection.setBaseUrl(baseURL);
		return connection;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

}
