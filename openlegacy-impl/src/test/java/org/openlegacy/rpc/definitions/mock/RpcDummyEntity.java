package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;
import org.openlegacy.rpc.actions.RpcActions.SHOW;

import java.util.Collections;
import java.util.List;

@RpcEntity(displayName = "Dummy Entity")
@RpcActions(actions = { @Action(action = READ.class, path = "/dir/program_name"),
		@Action(action = SHOW.class, path = "", rolesRequired = true, roles = { "AGENT", "MANAGER" }) })
@RpcNavigation(category = "Tree1")
public class RpcDummyEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcField(direction = Direction.INPUT, length = 20, key = true)
	String firstName;

	@RpcField(direction = Direction.INPUT, length = 20, key = true)
	String lastName;

	@RpcField(direction = Direction.INPUT, length = 4, key = true)
	Integer age;

	@RpcField(direction = Direction.OUTPUT, length = 100, roles = { "AGENT" })
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

	public DummyPart getDummyPart() {
		return dummyPart;
	}

	@RpcPart
	public static class DummyPart {

		@RpcField(direction = Direction.OUTPUT, length = 20, roles = { "AGENT" })
		private String partName;

		public String getPartName() {
			return partName;
		}

	}

	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.emptyList();
	}
}
