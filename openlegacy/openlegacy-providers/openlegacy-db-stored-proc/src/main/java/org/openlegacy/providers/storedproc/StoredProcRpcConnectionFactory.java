package org.openlegacy.providers.storedproc;

import org.openlegacy.rpc.LiveRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.springframework.beans.factory.InitializingBean;

public class StoredProcRpcConnectionFactory implements LiveRpcConnectionFactory, InitializingBean {

	private String dbUrl;
	private String dbDriverClassName;

	@Override
	public RpcConnection getConnection() {
		return new StoredProcRpcConnection(getDbUrl(), getDbDriverClassName());
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

	public String getDbDriverClassName() {
		return dbDriverClassName;
	}

	public void setDbDriverClassName(String dbDriverClassName) {
		this.dbDriverClassName = dbDriverClassName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
