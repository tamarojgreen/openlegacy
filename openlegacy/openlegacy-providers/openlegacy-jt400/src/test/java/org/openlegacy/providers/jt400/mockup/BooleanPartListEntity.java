package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.RpcActions.READ;

import java.util.List;

@RpcEntity(name = "BooleanPartListEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/BOOLIST.PGM") })
public class BooleanPartListEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcPartList(count = 2)
	private List<BooleanPart2> booleanPart2;

	public List<BooleanPart2> getBooleanPart2() {
		return booleanPart2;
	}

	public void setBooleanPart2(List<BooleanPart2> booleanPart2) {
		this.booleanPart2 = booleanPart2;
	}

	@RpcPart(name = "booleanPart2", legacyContainerName = "stam")
	public static class BooleanPart2 {

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
