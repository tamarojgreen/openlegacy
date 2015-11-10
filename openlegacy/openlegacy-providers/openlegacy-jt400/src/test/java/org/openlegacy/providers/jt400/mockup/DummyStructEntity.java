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

@RpcEntity(name = "DummyStructEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ROICBL2.PGM") })
public class DummyStructEntity implements org.openlegacy.rpc.RpcEntity {

	Record record;

	public Record getRecored() {
		return record;
	}

	public void setRecored(Record record) {
		this.record = record;

	}

	@RpcPart(name = "record")
	public static class Record {

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

	@Override
	public List<RpcActionDefinition> getActions() {
		return new ArrayList<RpcActionDefinition>();
	}
}
