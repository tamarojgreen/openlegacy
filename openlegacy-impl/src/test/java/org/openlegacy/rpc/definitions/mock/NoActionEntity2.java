package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.definitions.RpcActionDefinition;

import java.util.Collections;
import java.util.List;

@RpcEntity(displayName = "Dummy Entity without action 2")
public class NoActionEntity2 implements org.openlegacy.rpc.RpcEntity {

	@RpcField(direction = Direction.INPUT, length = 20)
	String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.emptyList();
	}

}
