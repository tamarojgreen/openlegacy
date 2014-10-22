package org.openlegacy.designtime.rpc.generators;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.RpcEntityType.MasterDetails;

import java.util.List;

@RpcEntity(rpcType = MasterDetails.class)
public class MasterDummyEntity {

	private Parent parent;

	@RpcPart
	public static class Parent {

		@RpcNumericField(minimumValue = -999999999, maximumValue = 999999999, decimalPlaces = 0)
		@RpcField(length = 9)
		private Integer id;

		@RpcField(length = 25)
		private String name;

		@RpcField(length = 10)
		private String phone;

		@RpcPartList(count = 10)
		private List<DetailSummary> detailSumaries;
	}

	@RpcActions(actions = { @Action(action = org.openlegacy.rpc.actions.RpcActions.READ.class, path = "", targetEntity = DetailsDummyEntity.class) })
	@RpcPart
	public static class DetailSummaries {

		@RpcField(length = 5, key = true)
		private String id;

		@RpcField(length = 10)
		private String name;
	}
}
