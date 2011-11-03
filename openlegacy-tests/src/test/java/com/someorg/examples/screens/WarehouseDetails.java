package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.actions.SendKeyClasses.PF3;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 38, value = "Warehouse Details"),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "2") }, exitAction = PF3.class)
public class WarehouseDetails {

}
