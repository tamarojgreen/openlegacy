package org.openlegacy.rpc;

import org.openlegacy.Snapshot;

import java.io.Serializable;

public interface RpcSnapshot extends Snapshot, Serializable {

	Integer getSequence();

	RpcInvokeAction getRpcInvokeAction();

	RpcResult getRpcResult();
}
