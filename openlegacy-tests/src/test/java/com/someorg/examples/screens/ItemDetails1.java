package com.someorg.examples.screens;

import org.openlegacy.FetchMode;
import org.openlegacy.annotations.screen.ChildScreenEntity;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.support.actions.SendKeyClasses;

@ScreenEntity(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number . ."),
		@Identifier(row = 7, column = 2, value = "Item Description") })
public class ItemDetails1 {

	@FieldMapping(row = 6, column = 33)
	private String ItemNumber;

	@ChildScreenEntity(stepInto = SendKeyClasses.ENTER.class, fetchMode = FetchMode.LAZY)
	private ItemDetails2 itemDetails2;

}
