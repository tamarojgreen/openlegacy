package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(name = "TreeArray")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/RPGROICH.PGM") })
public class DummyFlatEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(length = 20, originalName = "firstName")
	String firstName;

	@RpcField(length = 20, originalName = "lastName")
	String lastName;

	@RpcField(length = 30, originalName = "resultName")
	String resultName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getResultName() {
		return resultName;
	}

}
