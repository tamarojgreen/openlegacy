package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.springframework.beans.factory.InitializingBean;

public class StoredProcRpcConnectionFactory implements LiveRpcConnectionFactory, InitializingBean {

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

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
