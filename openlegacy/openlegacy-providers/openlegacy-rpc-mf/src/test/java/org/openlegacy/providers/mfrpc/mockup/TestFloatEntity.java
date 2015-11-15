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

@RpcEntity(name = "TestFloatEntity", language = Languages.COBOL)
@RpcActions(actions = { @Action(action = EXECUTE.class, path = "TNUM2", displayName = "Execute", alias = "execute") })
@RpcNavigation(category = "menu")
public class TestFloatEntity implements org.openlegacy.rpc.RpcEntity {

	private Dfhcommarea dfhcommarea = new Dfhcommarea();

	@RpcPart(name = "Dfhcommarea", originalName = "DFHCOMMAREA")
	public static class Dfhcommarea implements org.openlegacy.rpc.RpcEntity {

		@RpcNumericField(minimumValue = -99.99, maximumValue = 99.99, decimalPlaces = 2)
		@RpcField(length = 4, originalName = "TNUM-1", legacyType = "Num Assumed Decimal (+ve)")
		private Double tnum1;

		@RpcNumericField(minimumValue = -999.9, maximumValue = 999.9, decimalPlaces = 1)
		@RpcField(length = 4, originalName = "TNUM-2", legacyType = "Signed Decimal")
		private Double tnum2;

		@RpcNumericField(minimumValue = 0, maximumValue = 9.9, decimalPlaces = 1)
		@RpcField(length = 2, originalName = "TNUM-3", legacyType = "Num Assumed Decimal (+ve)")
		private Double tnum3;

		public Double getTnum3() {
			return tnum3;
		}

		public void setTnum3(Double tnum3) {
			this.tnum3 = tnum3;
		}

		public Double getTnum1() {
			return tnum1;
		}

		public void setTnum1(Double tnum1) {
			this.tnum1 = tnum1;
		}

		public Double getTnum2() {
			return tnum2;
		}

		public void setTnum2(Double tnum2) {
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
