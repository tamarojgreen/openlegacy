package com.test1;

import java.math.BigInteger;
import java.util.List;

import org.openlegacy.annotations.rpc.*;
import org.openlegacy.FieldType.*;
import org.openlegacy.rpc.RpcActions.*;

@RpcEntity(name="Items")
@RpcActions(actions = { 
				@Action(action = READ.class, path = "/QSYS.LIB/RMR2L1.LIB/ITEMS.PGM", displayName = "Read", alias = "read") 
				})
public class Items {

		@RpcPartList(count=5)
		private List<InnerRecord> innerRecord;

	@RpcPart(name="InnerRecord", legacyContainerName = "TopLevel")
	public static class InnerRecord {

		@RpcNumericField(minimumValue=-9999, maximumValue=9999, decimalPlaces=0)
		@RpcField(length = 4, originalName = "ITEM-NUMBER")
		private Integer itemNumber;

		@RpcField(length = 16, originalName = "ITEM-NAME")
		private String itemName;

		@RpcField(length = 28, originalName = "DESCRIPTION")
		private String description;

	}
}
