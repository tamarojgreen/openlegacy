package org.openlegacy.cache.tests.models;

import org.openlegacy.annotations.cache.Cacheable;
import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.rpc.actions.RpcActions.CREATE;
import org.openlegacy.rpc.actions.RpcActions.EXECUTE;
import org.openlegacy.rpc.actions.RpcActions.READ;
import org.openlegacy.rpc.actions.RpcActions.UPDATE;

@Cacheable(expiry = 300, getActions = { EXECUTE.class, READ.class }, removeActions = { CREATE.class, UPDATE.class })
@RpcEntity(name = "ItemDetails")
@RpcActions(actions = { @Action(action = EXECUTE.class, path = "getItemDetails", global = false),
		@Action(action = READ.class, path = "getItemDetails", global = false),
		@Action(action = UPDATE.class, path = "getItemDetails", global = false) })
public class ItemDetails implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -99999999, maximumValue = 99999999, decimalPlaces = 0)
	@RpcField(length = 8, key = true, originalName = "itemId", displayName = "Item Number", direction = Direction.INPUT, order = 0)
	private Integer itemNum;

	private ItemRecord itemRecord;
	private Shipping shipping;

	@RpcPart(name = "ItemRecord")
	public static class ItemRecord {

		@RpcField(length = 16, originalName = "name", direction = Direction.OUTPUT)
		private String itemName;

		@RpcField(length = 28, originalName = "description", direction = Direction.OUTPUT)
		private String description;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "weight", direction = Direction.OUTPUT)
		private Integer weight;
	}

	@RpcPart(name = "Shipping")
	public static class Shipping {

		@RpcField(length = 10, originalName = "shipping_method_name", direction = Direction.OUTPUT)
		private String shippingMethod;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "days", direction = Direction.OUTPUT)
		private Integer days;

	}

	@Override
	public boolean equals(Object obj) {
		ItemDetails itemDetails = (ItemDetails) obj;

		if (itemDetails.itemNum.intValue() != itemNum.intValue()) {
			return false;
		}

		if (!itemDetails.itemRecord.itemName.equals(itemRecord.itemName)) {
			return false;
		}

		if (!itemDetails.itemRecord.description.equals(itemRecord.description)) {
			return false;
		}

		if (itemDetails.itemRecord.weight.intValue() != itemRecord.weight.intValue()) {
			return false;
		}

		if (itemDetails.shipping != null && shipping != null) {
			if (!itemDetails.shipping.shippingMethod.equals(shipping.shippingMethod)) {
				return false;
			}

			if (itemDetails.shipping.days.intValue() != shipping.days.intValue()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemDetails clone() {
		ItemDetails itemDetails = new ItemDetails();
		itemDetails.itemNum = itemNum;

		itemDetails.itemRecord = new ItemRecord();
		itemDetails.itemRecord.itemName = itemRecord.itemName;
		itemDetails.itemRecord.description = itemRecord.description;
		itemDetails.itemRecord.weight = itemRecord.weight;

		itemDetails.shipping = new Shipping();
		itemDetails.shipping.shippingMethod = shipping.shippingMethod;
		itemDetails.shipping.days = shipping.days;

		return itemDetails;
	}
}
