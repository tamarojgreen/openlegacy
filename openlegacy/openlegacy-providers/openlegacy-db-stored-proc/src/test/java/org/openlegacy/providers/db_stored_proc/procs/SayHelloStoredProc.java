package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SayHelloStoredProc extends StoredProcEntity {

	private String param;

	public static class Results extends StoredProcResultSet {
		public String value;
	}

	@Override
	public void invokeStoredProc() {
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

			CallableStatement cs = connection.prepareCall("{call sayHello(?)}");
			cs.setString(1, param);

			ResultSet rs = cs.executeQuery();

			if (rs.next()) {
				Results rr = new Results();
				rr.value = rs.getString(1);

				results = rr;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fetchFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("param")) {
					param = (String) ff.getValue();
				}
			}
		}
	}

	@Override
	public void updateFields(List<RpcField> fields) {
		Results rr = (Results) results;

		for (RpcField f : fields) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("result")) {
					ff.setValue(rr.value);
				}
			}
		}
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
