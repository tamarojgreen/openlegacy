package org.openlegacy.rpc;

import org.openlegacy.Session;
import org.openlegacy.rpc.actions.RpcAction;

public interface RpcSession extends Session {

	<R extends RpcEntity> R doAction(RpcAction action, R rpcEntity);
}
