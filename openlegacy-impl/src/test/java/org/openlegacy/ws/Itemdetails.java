package org.openlegacy.ws;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.actions.RpcActions.EXECUTE;

@RpcNavigation(category = "ItemDetails")
@RpcEntity(name="Itemdetails")
@RpcActions(actions = { 
				@Action(action = EXECUTE.class, path = "/QSYS.LIB/RMR2L1.LIB/FULLDETAIL.PGM", displayName = "Execute", alias = "execute") 
				})
public class Itemdetails {

		@RpcNumericField(minimumValue=-99999999, maximumValue=99999999, decimalPlaces=0)
		@RpcField(length = 8, key = true, originalName = "ITEM-NUM")
		private Integer itemNum;

		private ItemRecord itemRecord = new ItemRecord();
		private Shipping shipping = new Shipping();

	@RpcPart(name="ItemRecord")
	public static class ItemRecord {

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

		@RpcNumericField(minimumValue=-9999, maximumValue=9999, decimalPlaces=0)
		@RpcField(length = 4, originalName = "WEIGHT")
		private Integer weight;

	}
	@RpcPart(name="Shipping")
	public static class Shipping {

		@RpcField(length = 10, originalName = "SHIPPING-METHOD")
		private String shippingMethod;

		@RpcNumericField(minimumValue=-9999, maximumValue=9999, decimalPlaces=0)
		@RpcField(length = 4, originalName = "DAYS")
		private Integer days;

	}
}
