package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.math.BigInteger;

@RpcEntity(name = "BigIntEntity")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/BIGINTCBL.PGM") })
public class BigIntEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -999999999999L, maximumValue = 999999999999L)
	@RpcField(length = 12, originalName = "bigInt", editable = true)
	BigInteger bigInt;

	public BigInteger getBigInt() {
		return bigInt;
	}

	public void setBigInt(BigInteger bigInt) {
		this.bigInt = bigInt;
	}

}
