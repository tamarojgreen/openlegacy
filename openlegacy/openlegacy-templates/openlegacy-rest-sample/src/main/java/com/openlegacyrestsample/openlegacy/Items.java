package com.openlegacyrestsample.openlegacy;

import java.util.List;

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

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F6.class, displayName = "Create", alias = "create"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = InventoryMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class Items {

	private List<ItemDetailesRecord> itemDetailesRecords;

	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "2", displayName = "Revise", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "revise"),
			@TableAction(actionValue = "4", displayName = "Delete", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "delete"),
			@TableAction(actionValue = "5", displayName = "Display", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "display") })
	public static class ItemDetailesRecord {

		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, displayName = "Actions")
		private String act;
		@ScreenColumn(startColumn = 6, endColumn = 15, displayName = "Alpha Search")
		private String alphaSearch;
		@ScreenColumn(startColumn = 19, endColumn = 58, displayName = "Item Description")
		private String itemDescription;
		@ScreenColumn(startColumn = 60, endColumn = 70, displayName = "Item Number", key = true)
		private String itemNumber;
	}
    


    


 
}
