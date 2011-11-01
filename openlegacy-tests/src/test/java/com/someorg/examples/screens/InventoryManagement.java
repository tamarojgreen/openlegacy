package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.SimpleScreenIdentifiers;

@ScreenEntity
@SimpleScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 31, value = "Inventory Management") })
@ScreenNavigation(accessedFrom = MainMenu.class, assignedFields = { @AssignedField(field = "selection", value = "1") })
public class InventoryManagement {

	@FieldMapping(row = 21, column = 8)
	private String selection;
}
