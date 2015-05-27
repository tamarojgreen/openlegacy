package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcResult;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoredProcRpcConnection implements RpcConnection {

	private Integer sequence = 0;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		String procName = "";
		int intParam1 = 0;
		int intParam2 = 0;
		String stringParam = "";

		RpcFlatField summResultField = null;
		RpcFlatField subResultField = null;
		RpcFlatField mulResultField = null;
		RpcFlatField stringResultField = null;

		for (RpcField f : rpcInvokeAction.getFields()) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;

				if (ff.getName().equals("procName")) {
					procName = (String) ff.getValue();
				} else if (ff.getName().equals("intParam1")) {
					intParam1 = ((BigDecimal) ff.getValue()).intValue();
				} else if (ff.getName().equals("intParam2")) {
					intParam2 = ((BigDecimal) ff.getValue()).intValue();
				} else if (ff.getName().equals("stringParam")) {
					stringParam = (String) ff.getValue();
				} else if (ff.getName().equals("summResult")) {
					summResultField = ff;
				} else if (ff.getName().equals("subResult")) {
					subResultField = ff;
				} else if (ff.getName().equals("mulResult")) {
					mulResultField = ff;
				} else if (ff.getName().equals("stringResult")) {
					stringResultField = ff;
				}
			}
		}

		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			// handle the error
		}

		String url = "jdbc:mysql://localhost:3306/rpc_test";
		String user = "rpc_test";
		String password = "password";

		try {
			Connection connection = DriverManager.getConnection(url, user,
					password);

			if (procName.equals("doStuffWithTwoNumbers")) {
				CallableStatement cs = connection
						.prepareCall("{call doStuffWithTwoNumbers(?, ?)}");
				cs.setInt(1, intParam1);
				cs.setInt(2, intParam2);

				ResultSet rs = cs.executeQuery();

				if (rs.next()) {
					summResultField.setValue(rs.getInt(1));
					subResultField.setValue(rs.getInt(2));
					mulResultField.setValue(rs.getInt(3));
				}
			} else if (procName.equals("sayHello")) {
				CallableStatement cs = connection
						.prepareCall("{call sayHello(?)}");
				cs.setString(1, stringParam);

				ResultSet rs = cs.executeQuery();

				if (rs.next()) {
					stringResultField.setValue(rs.getString(1));
				}
			}

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
		// TODO Auto-generated method stub

	}

}
