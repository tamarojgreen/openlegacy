package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(displayName = "Dummy Entity")
@RpcActions(actions = { @Action(action = READ.class, path = "/dir/program_name") })
@RpcNavigation(category = "Tree1")
public class RpcDummyEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(direction = Direction.INPUT, length = 20, key = true)
	String firstName;

	@RpcField(direction = Direction.INPUT, length = 20, key = true)
	String lastName;

	@RpcField(direction = Direction.INPUT, length = 4, key = true)
	Integer age;

	@RpcField(direction = Direction.OUTPUT, length = 100)
	String message;

	DummyPart dummyPart;

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@RpcPart
	public static class DummyPart {

	}
}
