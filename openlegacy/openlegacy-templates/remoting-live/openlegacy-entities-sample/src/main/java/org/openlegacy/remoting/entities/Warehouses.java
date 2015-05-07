package org.openlegacy.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.terminal.ScreenEntity.NONE;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.actions.TerminalActions.F12;

import java.io.Serializable;
import java.util.List;

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "  Work with Warehouse Details "),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.                                    ") })
@ScreenActions(actions = { @Action(action = TerminalActions.F6.class, displayName = "Create", alias = "create"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = InventoryMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "2") })
public class Warehouses implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<WarehousesRecord> warehousesRecords;

	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "2", displayName = "Revise", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "revise"),
			@TableAction(actionValue = "4", displayName = "Delete", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "delete"),
			@TableAction(actionValue = "5", displayName = "Display", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "display"),
			@TableAction(actionValue = "6", displayName = "Items", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "items"),
			@TableAction(actionValue = "7", displayName = "Locations", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "locations") })
	public static class WarehousesRecord implements Serializable {

		private static final long serialVersionUID = 1L;
		@ScreenColumn(startColumn = 4, endColumn = 4, editable = true, displayName = "Action")
		private String act;
		@ScreenColumn(startColumn = 10, endColumn = 12, displayName = "Serial Number", key = true)
		private String serialNumber;
		@ScreenColumn(startColumn = 16, endColumn = 55, displayName = "Description")
		private String description;
	}

}
