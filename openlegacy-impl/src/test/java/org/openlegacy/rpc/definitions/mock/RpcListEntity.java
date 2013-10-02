package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcListField;
import org.openlegacy.annotations.rpc.RpcNumericField;

import java.util.List;

@RpcEntity(name = "RpcList")
public class RpcListEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcListField(count = 5)
	@RpcField(length = 10, originalName = "FIELD")
	private List<String> field1;

	@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
	@RpcListField(count = 3)
	@RpcField(length = 2, originalName = "FIELD")
	private List<Integer> field2;

	public List<String> getField1() {
		return field1;
	}

	public void setField1(List<String> field1) {
		this.field1 = field1;
	}

	public List<Integer> getField2() {
		return field2;
	}

	public void setField2(List<Integer> field2) {
		this.field2 = field2;
	}
}