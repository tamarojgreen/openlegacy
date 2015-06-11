package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;

public class StoredProcRpcConnectionFactory implements RpcConnectionFactory {

	private String dbUrl;

	@Override
	public RpcConnection getConnection() {
		return new StoredProcRpcConnection(getDbUrl());
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

}
