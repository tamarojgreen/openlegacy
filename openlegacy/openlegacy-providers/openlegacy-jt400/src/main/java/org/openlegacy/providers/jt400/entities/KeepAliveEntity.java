package org.openlegacy.providers.jt400.entities;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.rpc.actions.RpcActions.READ;

@RpcEntity(language = Languages.COBOL)
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/DUMMY.PGM", global = false), })
public class KeepAliveEntity implements org.openlegacy.rpc.RpcEntity {

}
