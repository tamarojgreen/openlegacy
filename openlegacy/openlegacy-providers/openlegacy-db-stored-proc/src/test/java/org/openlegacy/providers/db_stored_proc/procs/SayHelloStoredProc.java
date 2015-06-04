package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.providers.db_stored_proc.AbstractDatabaseStoredProcedure;
import org.openlegacy.providers.db_stored_proc.entities.SayHelloEntity;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SayHelloStoredProc extends AbstractDatabaseStoredProcedure {

	SayHelloEntity entity = new SayHelloEntity();

	@Override
	public void invoke(Connection connection) {
		try {
			CallableStatement cs = connection.prepareCall("{call sayHello(?)}");
			cs.setString(1, entity.param);

			ResultSet rs = cs.executeQuery();

			if (rs.next()) {
				entity.value = rs.getString(1);
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
					entity.param = (String) ff.getValue();
				}
			}
		}
	}

	@Override
	public void updateFields(List<RpcField> fields) {
		for (RpcField f : fields) {
			if (f instanceof RpcFlatField) {
				RpcFlatField ff = (RpcFlatField) f;
				if (ff.getName().equals("result")) {
					ff.setValue(entity.value);
				}
			}
		}
	}
}
