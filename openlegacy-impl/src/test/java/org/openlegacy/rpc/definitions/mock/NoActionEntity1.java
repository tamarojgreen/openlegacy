package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;

@RpcEntity(displayName = "Dummy Entity without action 1")
public class NoActionEntity1 implements org.openlegacy.rpc.RpcEntity {

	@RpcField(direction = Direction.INPUT, length = 20)
	String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
