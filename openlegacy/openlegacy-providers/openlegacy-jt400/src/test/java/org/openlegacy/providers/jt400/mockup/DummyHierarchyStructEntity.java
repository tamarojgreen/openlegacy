package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(name = "DummyHierarchyStructEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/TREECBL.PGM") })
public class DummyHierarchyStructEntity implements org.openlegacy.rpc.RpcEntity {

	private Top top;

	public Top getTop() {
		return top;
	}

	public void setTop(Top top) {
		this.top = top;
	}

	@RpcPart(name = "Top")
	public static class Top {

		private InnerRecord innerRecord;

		public InnerRecord getRecord() {
			return innerRecord;
		}

		public void setRecord(InnerRecord record) {
			this.innerRecord = record;
		}
	}

	@RpcPart(name = "InnerRecord")
	public static class InnerRecord {

		@RpcField(length = 2, originalName = "field1")
		Byte field1 = 0;

		@RpcField(length = 2, originalName = "field2", defaultValue = "0")
		Byte field2 = 0;

		public Byte getField1() {
			return field1;
		}

		public void setField1(Byte field1) {
			this.field1 = field1;
		}

		public Byte getField2() {
			return field2;
		}

		public void setField2(Byte field2) {
			this.field2 = field2;
		}

	}
}