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

public class DoStuffWithTwoNumbersStoredProc extends StoredProcEntity {

	int param1;
	int param2;

	public static class Results extends StoredProcResultSet {
		public int sum;
		public int sub;
		public int mul;
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

			CallableStatement cs = connection
					.prepareCall("{call doStuffWithTwoNumbers(?, ?)}");
			cs.setInt(1, param1);
			cs.setInt(2, param2);

			ResultSet rs = cs.executeQuery();

			if (rs.next()) {
				Results rr = new Results();

				rr.sum = rs.getInt(1);
				rr.sub = rs.getInt(2);
				rr.mul = rs.getInt(3);

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
				if (ff.getName().equals("param1")) {
					param1 = ((BigDecimal) ff.getValue()).intValue();
				} else if (ff.getName().equals("param2")) {
					param2 = ((BigDecimal) ff.getValue()).intValue();
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
				if (ff.getName().equals("sum")) {
					ff.setValue(rr.sum);
				} else if (ff.getName().equals("sub")) {
					ff.setValue(rr.sub);
				} else if (ff.getName().equals("mul")) {
					ff.setValue(rr.mul);
				}
			}
		}
	}

	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}

	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}

}
