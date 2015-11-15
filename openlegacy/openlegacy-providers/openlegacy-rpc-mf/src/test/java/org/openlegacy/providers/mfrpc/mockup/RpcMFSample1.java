package org.openlegacy.providers.mfrpc.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.rpc.actions.RpcActions.EXECUTE;

@RpcNavigation(category = "")
@RpcEntity(name = "Rpc")
@RpcActions(actions = { @Action(action = EXECUTE.class, path = "RPC", displayName = "Execute", alias = "execute") })
public class RpcMFSample1 implements org.openlegacy.rpc.RpcEntity {

	@RpcField(length = 8, originalName = "CA-NAME", legacyType = "Char")
	private String caName;

	@RpcField(length = 20, originalName = "CA-LAST", legacyType = "Char")
	private String caLast;

	@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
	@RpcField(length = 2, originalName = "CA-AGE", legacyType = "Binary Integer")
	private Integer caAge;

	@RpcNumericField(minimumValue = -99999999, maximumValue = 99999999, decimalPlaces = 0)
	@RpcField(length = 4, originalName = "CA-SALARY", legacyType = "Binary Integer")
	private Integer caSalary;

	public String getCaName() {
		return caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}

	public String getCaLast() {
		return caLast;
	}

	public void setCaLast(String caLast) {
		this.caLast = caLast;
	}

	public Integer getCaAge() {
		return caAge;
	}

	public void setCaAge(Integer caAge) {
		this.caAge = caAge;
	}

	public Integer getCaSalary() {
		return caSalary;
	}

	public void setCaSalary(Integer caSalary) {
		this.caSalary = caSalary;
	}

}
