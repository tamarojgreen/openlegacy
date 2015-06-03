package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.sql.SQLException;

import javax.sql.DataSource;

public class StoredProcRpcConnection implements RpcConnection {

	private Integer sequence = 0;

	private DataSource dataSource;

	public StoredProcRpcConnection(DataSource dataSource) {
		this.dataSource = dataSource;
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
		String className = "";

		for (RpcField f : rpcInvokeAction.getFields()) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("className")) {
					className = (String) ff.getValue();
				}
			}
		}

		try {
			StoredProcEntity storedProc = (StoredProcEntity) Class.forName(
					className).newInstance();

			storedProc.fetchFields(rpcInvokeAction.getFields());
			storedProc.invokeStoredProc(dataSource.getConnection());
			storedProc.updateFields(rpcInvokeAction.getFields());
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

}
