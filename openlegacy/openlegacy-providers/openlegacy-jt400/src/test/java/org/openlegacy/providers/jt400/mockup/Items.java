package org.openlegacy.providers.jt400.mockup;

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

@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMS.PGM", global = false) })
@RpcEntity(name = "Items")
@RpcNavigation(category = "Inventory Menu")
public class Items implements org.openlegacy.rpc.RpcEntity {

	@RpcPartList(count = 5)
	private List<InnerRecord> innerRecord;

	@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMDETAIL.PGM", displayName = "View", targetEntity = ItemDetails.class, alias = "display") })
	@RpcPart(name = "InnerRecord", legacyContainerName = "Top")
	public static class InnerRecord {

		@RpcNumericField(minimumValue = -9999, maximumValue = 9999, decimalPlaces = 0)
		@RpcField(length = 4, key = true, originalName = "ITEM-NUMBER")
		private Integer itemNumber;

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

		public Integer getItemNumber() {
			return itemNumber;
		}

		public void setItemNumber(Integer itemNumber) {
			this.itemNumber = itemNumber;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	public List<InnerRecord> getInnerRecord() {
		return innerRecord;
	}

	public void setInnerRecord(List<InnerRecord> innerRecord) {
		this.innerRecord = innerRecord;
	}
}
