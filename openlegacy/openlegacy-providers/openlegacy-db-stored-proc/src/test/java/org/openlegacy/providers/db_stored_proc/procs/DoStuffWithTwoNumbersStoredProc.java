package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.providers.db_stored_proc.AbstractDatabaseStoredProcedure;
import org.openlegacy.providers.db_stored_proc.entities.DoStuffWithTwoNumbersEntity;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DoStuffWithTwoNumbersStoredProc extends AbstractDatabaseStoredProcedure {

	DoStuffWithTwoNumbersEntity entity = new DoStuffWithTwoNumbersEntity();

	@Override
	public void invoke(Connection connection) {
		try {
			CallableStatement cs = connection.prepareCall("{call doStuffWithTwoNumbers(?, ?)}");
			cs.setInt(1, entity.param1);
			cs.setInt(2, entity.param2);

			ResultSet rs = cs.executeQuery();

			if (rs.next()) {
				entity.sum = rs.getInt(1);
				entity.sub = rs.getInt(2);
				entity.mul = rs.getInt(3);
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
					entity.param1 = ((BigDecimal) ff.getValue()).intValue();
				} else if (ff.getName().equals("param2")) {
					entity.param2 = ((BigDecimal) ff.getValue()).intValue();
				}
			}
		}
	}

	@Override
	public void updateFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("sum")) {
					ff.setValue(entity.sum);
				} else if (ff.getName().equals("sub")) {
					ff.setValue(entity.sub);
				} else if (ff.getName().equals("mul")) {
					ff.setValue(entity.mul);
				}
			}
		}
	}
}
