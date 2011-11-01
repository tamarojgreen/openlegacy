package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.SimpleScreenIdentifiers;

@ScreenEntity
@SimpleScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number/Desc :"),
		@Identifier(row = 7, column = 2, value = "N/L Cost of Sales Account") })
public class ItemDetails2 {

	@FieldMapping(row = 6, column = 22)
	private String itemNumber;
}
