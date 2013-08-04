package org.openlegacy.rpc.samples.model;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.rpc.RpcActions.READ;
import org.openlegacy.rpc.RpcActions.UPDATE;

@RpcEntity(language = Languages.RPG)
@RpcActions(actions = { 
		@Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/GETITEMDET.PGM", global = false),
		@Action(action = UPDATE.class, path = "/QSYS.LIB/RMR2L1.LIB/UPDITEMDET.PGM") })
public class ItemDetails implements org.openlegacy.rpc.RpcEntity {

	@RpcField(originalName = "item_number", direction = Direction.INPUT, length = 6, key = true)
	Integer itemNumber;

	@RpcField(originalName = "item_name", direction = Direction.OUTPUT, length = 20)
	String itemName;

	@RpcField(originalName = "item_description", direction = Direction.OUTPUT, length = 50)
	String itemDescription;

	@RpcField(originalName = "item_weight", direction = Direction.OUTPUT, length = 3)
	Integer itemWeight;

}
