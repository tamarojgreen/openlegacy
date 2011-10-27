package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(identifiers = { @Identifier(row = 1, column = 31, value = "Inventory Management") })
public class InventoryManagement {

	@FieldMapping(row = 21, column = 8)
	private String Selection;
}
