package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(name = "DummyWarpStructEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/TREECBL2.PGM") })
public class DummyLegacyContainerEntity implements org.openlegacy.rpc.RpcEntity {

	private Record record;

	@RpcPart(name = "Record", legacyContainerName = "top")
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

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record
	 *            the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

}