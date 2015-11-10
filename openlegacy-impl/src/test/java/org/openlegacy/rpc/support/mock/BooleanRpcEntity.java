package org.openlegacy.rpc.support.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.Collections;
import java.util.List;

@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/BOOLEAN.PGM", global = false) })
@RpcEntity(name = "BooleanRpcEntity")
public class BooleanRpcEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "booleanTrue", editable = true)
	Boolean booleanTrue;

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "booleanFalse", editable = true)
	Boolean booleanFalse;

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "booleanEmpty", editable = true)
	Boolean booleanEmpty;

	PartBoolean partBoolean;

	public PartBoolean getPartBoolean() {
		return partBoolean;
	}

	public void setPartBoolean(PartBoolean partBoolean) {
		this.partBoolean = partBoolean;
	}

	@RpcPart(name = "partBoolean")
	public static class PartBoolean {

		@RpcBooleanField(falseValue = "N", trueValue = "Y")
		@RpcField(length = 1, originalName = "booleanInPart", editable = true)
		Boolean booleanInPart;

		public Boolean getBooleanInPart() {
			return booleanInPart;
		}

		public void setBooleanInPart(Boolean booleanInPart) {
			this.booleanInPart = booleanInPart;
		}
	}

	public Boolean getBooleanTrue() {
		return booleanTrue;
	}

	public void setBooleanTrue(Boolean booleanTrue) {
		this.booleanTrue = booleanTrue;
	}

	public Boolean getBooleanFalse() {
		return booleanFalse;
	}

	public void setBooleanFalse(Boolean booleanFalse) {
		this.booleanFalse = booleanFalse;
	}

	public Boolean getBooleanEmpty() {
		return booleanEmpty;
	}

	public void setBooleanEmpty(Boolean booleanEmpty) {
		this.booleanEmpty = booleanEmpty;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.emptyList();
	}

}
