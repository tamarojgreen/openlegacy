package org.openlegacy.rpc;

import org.openlegacy.Snapshot;

import java.util.List;

public interface RpcResult extends Snapshot {

	List<RpcField> getRpcFields();
}
