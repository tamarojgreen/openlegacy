package org.openlegacy.providers.db_stored_proc;

import org.openlegacy.rpc.RpcField;

import java.sql.Connection;
import java.util.List;

public abstract class AbstractDatabaseStoredProcedure {

	public void fetchFields(List<RpcField> fields) {
	}

	public void updateFields(List<RpcField> fields) {
	}

	abstract public void invoke(Connection connection);

}
