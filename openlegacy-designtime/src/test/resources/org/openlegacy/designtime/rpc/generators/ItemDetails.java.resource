package org.openlegacy.designtime.rpc.generators.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.RpcActions.READ;
import org.openlegacy.rpc.RpcActions.UPDATE;

@SuppressWarnings("unused")
@RpcEntity(name = "ItemDetails")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/FULLDETAIL.PGM", global = false),
		@Action(action = UPDATE.class, path = "/QSYS.LIB/RMR2L1.LIB/UPDATEITEM.PGM") })
public class ItemDetails implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -99999999, maximumValue = 99999999, decimalPlaces = 0)
	@RpcField(length = 8, key = true, originalName = "ITEM-NUM")
	private Integer itemNum;

	private ItemRecord itemRecord;
	private Shipping shipping;

	@RpcPart(name = "ItemRecord")
	public static class ItemRecord {

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "WEIGHT")
		private Integer weight;

	}

	@RpcPart(name = "Shipping")
	public static class Shipping {

		@RpcField(length = 10, originalName = "SHIPPING-METHOD")
		private String shippingMethod;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "DAYS")
		private Integer days;

	}
}
