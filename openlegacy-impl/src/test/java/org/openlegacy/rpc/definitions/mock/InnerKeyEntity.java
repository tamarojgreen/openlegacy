package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(displayName = "Inner key Entity")
@RpcActions(actions = { @Action(action = READ.class, path = "/dir/InnerKeyEntity") })
public class InnerKeyEntity implements org.openlegacy.rpc.RpcEntity {

	public InnerKeyEntity() {
		containsKeyPart = new ContainsKeyPart();
	}

	public String getTopLevelKey() {
		return topLevelKey;
	}

	public void setTopLevelKey(String topLevelKey) {
		this.topLevelKey = topLevelKey;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@RpcField(direction = Direction.INPUT, length = 20, key = true, keyIndex = 2)
	String topLevelKey;

	@RpcField(direction = Direction.INPUT, length = 20)
	String result;

	ContainsKeyPart containsKeyPart;

	public ContainsKeyPart getContainsKeyPart() {
		return containsKeyPart;
	}

	public void setContainsKeyPart(ContainsKeyPart containsKeyPart) {
		this.containsKeyPart = containsKeyPart;
	}

	@RpcPart(name = "containsKeyPart")
	public static class ContainsKeyPart {

		public ContainsKeyPart() {
			innerKeyPart = new InnerKeyPart();
		}

		InnerKeyPart innerKeyPart;

		public InnerKeyPart getInnerKeyPart() {
			return innerKeyPart;
		}

		public void setInnerKeyPart(InnerKeyPart innerKeyPart) {
			this.innerKeyPart = innerKeyPart;
		}

		@RpcPart
		public static class InnerKeyPart {

			@RpcField(direction = Direction.INPUT, length = 20, key = true, keyIndex = 1)
			String innerKey;

			public String getInnerKey() {
				return innerKey;
			}

			public void setInnerKey(String innerKey) {
				this.innerKey = innerKey;
			}

		}

	}
}
