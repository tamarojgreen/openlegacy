package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.SendKeyClasses.F3;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 30, value = "Work with Item Master"),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "1") }, exitAction = F3.class)
public class ItemsList {

	@FieldMapping(row = 21, column = 19)
	private String positionTo;
}
