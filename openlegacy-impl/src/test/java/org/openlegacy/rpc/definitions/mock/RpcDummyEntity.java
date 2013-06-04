package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity()
@RpcActions(actions = { @Action(action = READ.class, path = "/dir/program_name") })
public class RpcDummyEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(direction = Direction.INPUT, length = 20)
	String firstName;

	@RpcField(direction = Direction.INPUT, length = 20)
	String lastName;

	@RpcField(direction = Direction.INPUT, length = 3)
	Integer age;

	@RpcField(direction = Direction.OUTPUT, length = 100)
	String message;

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

}
