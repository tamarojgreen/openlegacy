package org.openlegacy.providers.db_stored_proc.procs;

import org.openlegacy.rpc.RpcField;

import java.util.List;

public abstract class StoredProcEntity {

	public static class StoredProcResultSet {
	}

	protected String className;
	StoredProcResultSet results;

	public void fetchFields(List<RpcField> fields) {

	}

	public void updateFields(List<RpcField> fields) {

	}

	abstract public void invokeStoredProc();

	public StoredProcResultSet unrollResult() {
		return results;
	}
}
