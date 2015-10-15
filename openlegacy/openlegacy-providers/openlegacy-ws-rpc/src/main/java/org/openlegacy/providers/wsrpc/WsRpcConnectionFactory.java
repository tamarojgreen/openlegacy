package org.openlegacy.providers.wsrpc;

import org.openlegacy.exceptions.OpenlegacyRemoteRuntimeException;
import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;

import java.rmi.RemoteException;

public class WsRpcConnectionFactory implements LiveRpcConnectionFactory {

	private UrlProps props = new UrlProps();

	public WsRpcConnectionFactory() {
		this("");
	}

	public WsRpcConnectionFactory(String baseURL) {
		this.props.setBaseUrl(baseURL);
	}

	public WsRpcConnectionFactory(UrlProps props) {
		this.props = props;
	}

	@Override
	public RpcConnection getConnection() {
		WsRpcConnection connection = new WsRpcConnection(props);
		return connection;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		try {
			rpcConnection.disconnect();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

}