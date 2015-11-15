package org.openlegacy.providers.mfrpc.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.actions.RpcActions.EXECUTE;

@RpcEntity(name = "TestIntegerEntity", language = Languages.COBOL)
@RpcActions(actions = { @Action(action = EXECUTE.class, path = "TNUM1", displayName = "Execute", alias = "execute") })
@RpcNavigation(category = "menu")
public class TestIntegerEntity implements org.openlegacy.rpc.RpcEntity {

	private Dfhcommarea dfhcommarea = new Dfhcommarea();

	@RpcPart(name = "Dfhcommarea", originalName = "DFHCOMMAREA")
	public static class Dfhcommarea implements org.openlegacy.rpc.RpcEntity {

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "TNUM", legacyType = "Positive Zero Padded Number")
		private Integer tnum;

		@RpcNumericField(minimumValue = -99, maximumValue = 99, decimalPlaces = 0)
		@RpcField(length = 2, originalName = "TNUM2", legacyType = "Signed Decimal")
		private Integer tnum2;

		public Integer getTnum() {
			return tnum;
		}

		public void setTnum(Integer tnum) {
			this.tnum = tnum;
		}

		public Integer getTnum2() {
			return tnum2;
		}

		public void setTnum2(Integer tnum2) {
			this.tnum2 = tnum2;
		}

	}

	public Dfhcommarea getDfhcommarea() {
		return dfhcommarea;
	}

	public void setDfhcommarea(Dfhcommarea dfhcommarea) {
		this.dfhcommarea = dfhcommarea;
	}

}
