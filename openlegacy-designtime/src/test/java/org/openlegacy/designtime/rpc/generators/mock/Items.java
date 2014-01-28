package org.openlegacy.designtime.rpc.generators.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.rpc.RpcPartList;
import org.openlegacy.rpc.RpcActions.READ;
import org.openlegacy.rpc.RpcActions.UPDATE;

import java.util.List;

@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMS.PGM") })
@RpcEntity(name = "Items")
@RpcNavigation(category = "Inventory Menu")
@SuppressWarnings("unused")
public class Items implements org.openlegacy.rpc.RpcEntity {

	private TopLevel topLevel;

	@RpcPart(name = "TopLevel")
	public static class TopLevel {

		@RpcPartList(count = 5)
		private List<InnerRecord> innerRecord;
	}

	@RpcActions(actions = {
			@Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMDETAIL.PGM", displayName = "View", targetEntity = ItemDetails.class, alias = "display"),
			@Action(action = UPDATE.class, path = "/QSYS.LIB/RMR2L1.LIB/UPDITEMDET.PGM", displayName = "Edit", targetEntity = ItemDetails.class) })
	@RpcPart(name = "InnerRecord")
	public static class InnerRecord {

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, key = true, originalName = "ITEM-NUMBER")
		private Integer itemNumber;

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

	}
}
