package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.FieldType.General;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;

@RpcEntity(name = "SimpleField")
public class rpcSimpleEntity {

	@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
	@RpcField(length = 2, originalName = "PARAM1", fieldType = General.class)
	private Integer param1;

	@RpcNumericField(minimumValue = -9.9, maximumValue = 9.9, decimalPlaces = 1)
	@RpcField(length = 1, originalName = "PARAM2", fieldType = General.class)
	private Double param2;

	@RpcField(length = 4, originalName = "PARAM3", fieldType = General.class)
	private String param3;

	public Integer getParam1() {
		return param1;
	}

	public void setParam1(Integer param1) {
		this.param1 = param1;
	}

	public Double getParam2() {
		return param2;
	}

	public void setParam2(Double param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

}