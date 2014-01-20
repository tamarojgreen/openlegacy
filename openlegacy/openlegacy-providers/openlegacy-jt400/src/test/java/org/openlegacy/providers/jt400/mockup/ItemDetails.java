package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.RpcActions.READ;

@RpcEntity(language = Languages.COBOL)
@RpcActions(actions = { @Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMDETAIL.PGM", global = false), })
public class ItemDetails implements org.openlegacy.rpc.RpcEntity {

	@RpcField(originalName = "item_number", direction = Direction.INPUT, length = 8, key = true)
	Integer itemNumber;

	@RpcField(originalName = "item_name", direction = Direction.OUTPUT, length = 16)
	String itemName;

	@RpcField(originalName = "item_description", direction = Direction.OUTPUT, length = 20)
	String itemDescription;

	@RpcField(originalName = "item_weight", direction = Direction.OUTPUT, length = 4)
	Integer itemWeight;

	public Integer getItemNumber() {
		return this.itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getItemDescription() {
		return this.itemDescription;
	}

	public Integer getItemWeight() {
		return this.itemWeight;
	}

}
