package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.sql.Connection;
import java.sql.SQLException;

public class StoredProcRpcConnection implements RpcConnection {

	private Integer sequence = 0;

	private StoredProcDbSession session;
	private Connection connection;

	public StoredProcRpcConnection(StoredProcDbSession session) {
		this.session = session;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		try {
			return (connection != null) && connection.isValid(10000);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			connection = null;
		}
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
			storedProc.invokeStoredProc(connection);
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
		}

		SimpleRpcResult rpcResult = new SimpleRpcResult();
		rpcResult.setRpcFields(rpcInvokeAction.getFields());

		sequence++;
		return rpcResult;
	}

	@Override
	public void login(String user, String password) {
		connection = session.getConnection(user, password);

	}

}
