package org.openlegacy.as400.menus;

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
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;
import org.openlegacy.terminal.actions.TerminalActions.F4;

import java.util.List;

@ScreenEntity(window = true, rows = 20, columns = 78)
@ScreenIdentifiers(identifiers = { @Identifier(row = 3, column = 34, value = "Select Printer"),
		@Identifier(row = 5, column = 5, value = "Type option below, then press Enter."),
		@Identifier(row = 8, column = 5, value = "Opt") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = ChangePrinterOutput.class, exitAction = F12.class, terminalAction = F4.class, assignedFields = { @AssignedField(field = "printerToUse") })
public class SelectPrinter {

	private List<SelectPrinterRecord> selectPrinterRecords;

	@ScreenTable(startRow = 9, endRow = 17)
	@ScreenTableActions(actions = { @TableAction(displayName = "select", actionValue = "") })
	public static class SelectPrinterRecord {

		@ScreenColumn(startColumn = 10, endColumn = 19, key = true, displayName = "Printer", mainDisplayField = true, sampleValue = "#D")
		private String printer;
		@ScreenColumn(startColumn = 34, endColumn = 75, displayName = "Status", sampleValue = "Stopped")
		private String status;

	}

}
