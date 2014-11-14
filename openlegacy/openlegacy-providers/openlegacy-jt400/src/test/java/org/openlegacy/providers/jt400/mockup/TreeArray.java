package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.actions.RpcActions.READ;

import java.util.List;

@RpcEntity(name = "TreeArray")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/TREEARRAY.PGM") })
public class TreeArray implements org.openlegacy.rpc.RpcEntity {

	private ArArray arArray;

	public ArArray getArArray() {
		return arArray;
	}

	public void setArArray(ArArray arArray) {
		this.arArray = arArray;
	}

	@RpcPart(name = "ArArray")
	public static class ArArray {

		@RpcPartList(count = 3)
		private List<ArArrayRecords> arArrayRecords;

		public List<ArArrayRecords> getArArrayRecords() {
			return arArrayRecords;
		}

		public void setArArrayRecords(List<ArArrayRecords> arArrayRecords) {
			this.arArrayRecords = arArrayRecords;
		}

		@RpcPart(name = "ArArrayRecords")
		public static class ArArrayRecords {

			@RpcField(length = 11, originalName = "AR-TEXT")
			private String arText;

			@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
			@RpcField(length = 4, originalName = "AR-NUM")
			private Integer arNum;

			public String getArText() {
				return arText;
			}

			public void setArText(String arText) {
				this.arText = arText;
			}

			public Integer getArNum() {
				return arNum;
			}

			public void setArNum(Integer arNum) {
				this.arNum = arNum;
			}

		}

	}
}