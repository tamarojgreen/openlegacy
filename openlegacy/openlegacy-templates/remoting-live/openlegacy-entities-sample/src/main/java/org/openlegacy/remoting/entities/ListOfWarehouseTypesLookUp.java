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

@ScreenEntity(window = true, screenType = LookupEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 4, column = 19, value = "    List of Warehouse Types   "),
		@Identifier(row = 6, column = 6, value = "Type options, press Enter.") })
@ScreenActions(actions = { @Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = WarehouseCreate.class, terminalAction = F4.class, exitAction = F12.class)
public class ListOfWarehouseTypesLookUp implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ListOfWarehouseTypesLookUpRecord> listOfWarehouseTypesLookUpRecords;

	@ScreenTable(startRow = 10, endRow = 14)
	@ScreenTableActions(actions = { @TableAction(actionValue = "1", displayName = "Select", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "select") })
	public static class ListOfWarehouseTypesLookUpRecord implements Serializable {

		private static final long serialVersionUID = 1L;
		@ScreenColumn(startColumn = 8, endColumn = 8, editable = true, displayName = "Action")
		private String action;
		@ScreenColumn(startColumn = 14, endColumn = 15, key = true, displayName = "Type")
		private String type;
		@ScreenColumn(startColumn = 20, endColumn = 59, displayName = "Description")
		private String description;

	}

}
