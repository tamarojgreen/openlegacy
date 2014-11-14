package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.actions.RpcActions.READ;

@RpcEntity(name = "BooleanEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/BOOLTEST.PGM") })
public class BooleanEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "bool", editable = true)
	Boolean bool;

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}
}
