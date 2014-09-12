package com.test1;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.RpcActions.READ;

import java.util.List;

@RpcEntity(name = "Items")
@RpcActions(actions = { @Action(action = READ.class, path = "XX", displayName = "Read", alias = "read") })
@RpcNavigation(category = "Inventory Menu")
public class Items {

	@RpcPartList(count = 5)
	private List<InnerRecord> innerRecord;

	@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/FULLDETAIL.PGM", displayName = "View", targetEntity = Itemdetail.class, alias = "display") })
	@RpcPart(name = "InnerRecord", legacyContainerName = "TopLevel")
	public static class InnerRecord {

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "ITEM-NUMBER")
		private Integer itemNumber;

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

	}
}
