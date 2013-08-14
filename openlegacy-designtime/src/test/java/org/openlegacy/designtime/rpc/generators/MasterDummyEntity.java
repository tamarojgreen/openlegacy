package org.openlegacy.designtime.rpc.generators;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.RpcEntityType.MasterDetailsEntity;

import java.util.List;

@RpcEntity(rpcType = MasterDetailsEntity.class)
public class MasterDummyEntity {

	private Parent parent;

	@RpcPart(name = "LkCustomer")
	public static class Parent {

		@RpcField(length = 1, originalName = "LK-CUST-IND")
		private String index;

		@RpcNumericField(minimumValue = -999999999, maximumValue = 999999999, decimalPlaces = 0)
		@RpcField(length = 9, originalName = "LK-CUST-ID")
		private Integer id;

		@RpcField(length = 25, originalName = "CUST-NAME")
		private String name;

		private String phone;

		@RpcPartList(count = 10)
		private List<DetailSummary> detailSumarries;
	}

	@RpcActions(actions = { @Action(action = org.openlegacy.rpc.RpcActions.READ.class, path = "", targetEntity = DetailsDummyEntity.class) })
	@RpcPart(name = "LkCards")
	public static class DetailSummary {

		@RpcField(length = 5, key = true)
		private String id;

		@RpcField(length = 10)
		private String name;
	}
}
