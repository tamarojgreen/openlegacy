package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcNumericField;
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.rpc.actions.RpcActions.READ;
import org.openlegacy.rpc.actions.RpcActions.UPDATE;

import java.util.Collections;
import java.util.List;

@RpcEntity(name = "ItemDetails")
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/FULLDETAIL.PGM", global = false),
		@Action(action = UPDATE.class, path = "/QSYS.LIB/RMR2L1.LIB/UPDATEITEM.PGM") })
public class ItemDetails implements org.openlegacy.rpc.RpcEntity {

	@RpcNumericField(minimumValue = -99999999, maximumValue = 99999999, decimalPlaces = 0)
	@RpcField(length = 8, key = true, originalName = "ITEM-NUM", displayName = "Item Number")
	private Integer itemNum;

	private ItemRecord itemRecord;
	private Shipping shipping;

	public Integer getItemNum() {
		return this.itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	public ItemRecord getItemRecord() {
		return this.itemRecord;
	}

	public void setItemRecord(ItemRecord itemRecord) {
		this.itemRecord = itemRecord;
	}

	public Shipping getShipping() {
		return this.shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	@RpcPart(name = "ItemRecord")
	public static class ItemRecord {

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "WEIGHT")
		private Integer weight;

		public String getItemName() {
			return itemName;
		}

		public String getDescription() {
			return description;
		}

		public Integer getWeight() {
			return weight;
		}

	}

	@RpcPart(name = "Shipping")
	public static class Shipping {

		@RpcField(length = 10, originalName = "SHIPPING-METHOD")
		private String shippingMethod;

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, originalName = "DAYS")
		private Integer days;

		public String getShippingMethod() {
			return shippingMethod;
		}

		public Integer getDays() {
			return days;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RpcActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}
}
