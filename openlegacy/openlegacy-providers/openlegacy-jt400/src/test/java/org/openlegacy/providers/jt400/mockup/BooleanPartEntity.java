package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.actions.RpcActions.READ;

@RpcEntity(name = "BooleanPartEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/BOOLPART.PGM") })
public class BooleanPartEntity implements org.openlegacy.rpc.RpcEntity {

	BooleanPart booleanPart;

	public BooleanPart getBooleanPart() {
		return booleanPart;
	}

	public void setBooleanPart(BooleanPart booleanPart) {
		this.booleanPart = booleanPart;
	}

	@RpcPart(name = "BooleanPart")
	public static class BooleanPart {

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
}
