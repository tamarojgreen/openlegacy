package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

public class StoredProcRpcConnection implements RpcConnection {

	private Integer sequence = 0;

	@Inject
	private DataSource dataSource;

	private Map<String, StoredProcEntity> knownProcedures = new HashMap<String, StoredProcEntity>();

	public StoredProcRpcConnection() {
	}

	@Override
	public RpcSnapshot getSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAction(RpcInvokeAction sendAction) {
		invoke(sendAction);
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public boolean isConnected() {
		try {
			return dataSource.getConnection().isValid(10000);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		String procName = rpcInvokeAction.getRpcPath();

		try {
			StoredProcEntity storedProc = knownProcedures.get(procName);

			storedProc.fetchFields(rpcInvokeAction.getFields());
			storedProc.invokeStoredProc(dataSource.getConnection());
			storedProc.updateFields(rpcInvokeAction.getFields());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SimpleRpcResult rpcResult = new SimpleRpcResult();
		rpcResult.setRpcFields(rpcInvokeAction.getFields());

		sequence++;
		return rpcResult;
	}

	@Override
	public void login(String user, String password) {
	}

	public void setKnownProcedures(Map<String, StoredProcEntity> knownProcedures) {
		this.knownProcedures = knownProcedures;
	}
}
