package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

public class StoredProcRpcConnectionFactory implements RpcConnectionFactory {

	@Inject
	private DataSource dataSource;

	@Override
	public RpcConnection getConnection() {
		return new StoredProcRpcConnection(dataSource);
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
	}

}
