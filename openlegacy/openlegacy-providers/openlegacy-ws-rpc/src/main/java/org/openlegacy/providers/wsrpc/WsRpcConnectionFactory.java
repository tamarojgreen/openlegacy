package org.openlegacy.providers.wsrpc;

import javax.inject.Inject;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.springframework.stereotype.Service;


public class WsRpcConnectionFactory implements RpcConnectionFactory{

	@Inject 
	WsRpcConnection rpcConnection;
	
	@Override
	public RpcConnection getConnection(){
		return rpcConnection;
	}

	@Override
	public void disconnect(RpcConnection rpcConnection){
		rpcConnection.disconnect();
	}

}
