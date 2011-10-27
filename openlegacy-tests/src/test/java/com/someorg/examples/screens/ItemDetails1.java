package com.someorg.examples.screens;

import org.openlegacy.adapter.terminal.SendKeyClasses;
import org.openlegacy.annotations.screen.ChildScreenEntity;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number . ."),
		@Identifier(row = 7, column = 2, value = "Item Description") })
public class ItemDetails1 {

	@FieldMapping(row = 6, column = 33)
	private String ItemNumber;

	@ChildScreenEntity(stepInto = SendKeyClasses.ENTER.class)
	private ItemDetails2 itemDetails2;

	public ItemDetails2 fetchItemDetails2() {
		return itemDetails2;
	}
}
