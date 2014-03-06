package org.openlegacy.providers.jt400;

import org.openlegacy.providers.jt400.entities.KeepAliveEntity;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;

public class Jt400KeepAliveAction implements RpcAction {

	public void perform(RpcSession session, Object entity, Object... keys) {

		session.doAction(RpcActions.READ(), new KeepAliveEntity());
	}

}
