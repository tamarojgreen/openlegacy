package org.openlegacy.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.terminal.ScreenEntity.NONE;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.actions.TerminalActions.F12;
import org.openlegacy.terminal.actions.TerminalActions.F4;

import java.io.Serializable;
import java.util.List;

@ScreenEntity(window = true, screenType = LookupEntity.class, displayName = "List Of Stock Groups")
@ScreenIdentifiers(identifiers = { @Identifier(row = 5, column = 20, value = "     List of stock groups     "),
		@Identifier(row = 7, column = 6, value = "Type '1' to Select.") })
@ScreenActions(actions = { @Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = ItemCreateMain.class, terminalAction = F4.class, exitAction = F12.class)
public class ListOfStockGroupsLookUp implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ListOfStockGroupsLookUpRecord> listOfStockGroupsLookUpRecords;

	@ScreenTable(startRow = 10, endRow = 14)
	@ScreenTableActions(actions = { @TableAction(actionValue = "1", displayName = "Select", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "select") })
	public static class ListOfStockGroupsLookUpRecord implements Serializable {

		private static final long serialVersionUID = 1L;
		@ScreenColumn(startColumn = 8, endColumn = 8, editable = true, displayName = "Action")
		private String action;
		@ScreenColumn(startColumn = 14, endColumn = 16, key = true, displayName = "Group")
		private String group;
		@ScreenColumn(startColumn = 21, endColumn = 31, displayName = "Description")
		private String description;
	}

}
