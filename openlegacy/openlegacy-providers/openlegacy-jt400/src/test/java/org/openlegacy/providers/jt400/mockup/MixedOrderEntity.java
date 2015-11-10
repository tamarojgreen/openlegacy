package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.ArrayList;
import java.util.List;

@RpcEntity(name = "MixedOrder")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/MIXED.PGM") })
public class MixedOrderEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(length = 10, originalName = "VAR1")
	private String var1;

	private Struct1 struct1;

	@RpcField(length = 10, originalName = "VAR2")
	private String var2;

	private Struct2 struct2;

	@RpcPart(name = "Struct1")
	public static class Struct1 {

		@RpcField(length = 10, originalName = "CHILD1")
		private String child1;

		@RpcField(length = 10, originalName = "CHILD2")
		private String child2;

		public String getChild1() {
			return child1;
		}

		public void setChild1(String child1) {
			this.child1 = child1;
		}

		public String getChild2() {
			return child2;
		}

		public void setChild2(String child2) {
			this.child2 = child2;
		}

	}

	@RpcPart(name = "Struct2")
	public static class Struct2 {

		@RpcField(length = 10, originalName = "CHILD3")
		private String child3;

		@RpcField(length = 10, originalName = "CHILD4")
		private String child4;

		public String getChild3() {
			return child3;
		}

		public void setChild3(String child3) {
			this.child3 = child3;
		}

		public String getChild4() {
			return child4;
		}

		public void setChild4(String child4) {
			this.child4 = child4;
		}

	}

	public String getVar1() {
		return var1;
	}

	public void setVar1(String var1) {
		this.var1 = var1;
	}

	public Struct1 getStruct1() {
		return struct1;
	}

	public void setStruct1(Struct1 struct1) {
		this.struct1 = struct1;
	}

	public String getVar2() {
		return var2;
	}

	public void setVar2(String var2) {
		this.var2 = var2;
	}

	public Struct2 getStruct2() {
		return struct2;
	}

	public void setStruct2(Struct2 struct2) {
		this.struct2 = struct2;
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return new ArrayList<RpcActionDefinition>();
	}
}
