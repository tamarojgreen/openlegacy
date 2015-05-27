package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;

public class StoredProcRpcConnectionFactory implements RpcConnectionFactory {

	@Override
	public RpcConnection getConnection() {
		
		return new StoredProcRpcConnection();
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		// TODO Auto-generated method stub

	}

}
