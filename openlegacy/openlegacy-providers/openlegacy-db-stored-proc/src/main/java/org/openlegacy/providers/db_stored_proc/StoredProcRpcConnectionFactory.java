package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public class StoredProcRpcConnectionFactory implements RpcConnectionFactory {

	@Inject
	private ApplicationContext applicationContext;

	@Override
	public RpcConnection getConnection() {
		StoredProcDbSession dbSession = applicationContext.getBean(StoredProcDbSession.class);
		return new StoredProcRpcConnection(dbSession);
	}

	@Override
	public void disconnect(RpcConnection rpcConnection) {
		rpcConnection.disconnect();
	}

}
