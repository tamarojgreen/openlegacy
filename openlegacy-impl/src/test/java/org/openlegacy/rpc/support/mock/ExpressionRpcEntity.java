package org.openlegacy.rpc.support.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/EXPRESSION.PGM", global = false) })
@RpcEntity(name = "ExpressionRpcEntity")
public class ExpressionRpcEntity implements org.openlegacy.rpc.RpcEntity {

	@RpcBooleanField(falseValue = "N", trueValue = "Y")
	@RpcField(length = 1, originalName = "booleanTrue", editable = true)
	Boolean booleanTrue;

	@RpcField(expression = "/Msg://", length = 20, originalName = "expression1", editable = true)
	String expression1;

	@RpcField(length = 10, originalName = "intExpression", editable = true)
	Integer intExpression;

	@RpcField(expression = "#field.value + 1", length = 10, originalName = "intExpression2", editable = true)
	Integer intExpression2;

	@RpcField(expression = "#field.value + intExpression2", length = 10, originalName = "intExpression3", editable = true)
	Integer intExpression3;

	@RpcField(expression = "null", length = 10)
	String stringExpressionNull;

	@RpcField(expression = "intExpression + intExpression2", length = 10, editable = false, direction = Direction.NONE)
	Integer intExpression1and2;

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

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}

	public String getExpression1() {
		return expression1;
	}

	public void setExpression1(String expression1) {
		this.expression1 = expression1;
	}

	public Integer getIntExpression() {
		return intExpression;
	}

	public void setIntExpression(Integer intExpression) {
		this.intExpression = intExpression;
	}

	public Integer getIntExpression2() {
		return intExpression2;
	}

	public void setIntExpression2(Integer intExpression2) {
		this.intExpression2 = intExpression2;
	}

	public Integer getIntExpression3() {
		return intExpression3;
	}

	public void setIntExpression3(Integer intExpression3) {
		this.intExpression3 = intExpression3;
	}

	public String getStringExpressionNull() {
		return stringExpressionNull;
	}

	public void setStringExpressionNull(String stringExpressionNull) {
		this.stringExpressionNull = stringExpressionNull;
	}

	public Integer getIntExpression1and2() {
		return intExpression1and2;
	}

	public void setIntExpression1and2(Integer intExpression1and2) {
		this.intExpression1and2 = intExpression1and2;
	}
}
