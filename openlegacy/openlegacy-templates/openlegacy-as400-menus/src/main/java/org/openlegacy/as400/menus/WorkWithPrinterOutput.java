package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 1, column = 29, value = "Work with Printer Output"),
		@Identifier(row = 5, column = 2, value = "Type options below, then press Enter.  To work with printers, press F22.      "),
		@Identifier(row = 9, column = 7, value = "Printer/") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F9.class, displayName = "Command line", alias = "commandLine"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F9.class, additionalKey = AdditionalKey.SHIFT, displayName = "Select assistance level", alias = "selectAssistanceLevel"),
		@Action(action = TerminalActions.F10.class, additionalKey = AdditionalKey.SHIFT, displayName = "Work with printers", alias = "workWithPrinters") })
@ScreenNavigation(accessedFrom = UserTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "5") })
public class WorkWithPrinterOutput {

	@ScreenField(row = 2, column = 72, endColumn = 79, labelColumn = 62, displayName = "System", sampleValue = "S1051C6E")
	private String system;

	@ScreenField(row = 3, column = 21, endColumn = 30, labelColumn = 2, displayName = "User", sampleValue = "RMR20924")
	private String user;

	private List<WorkWithPrinterOutputRecord> workWithPrinterOutputRecords;

	@ScreenTable(endRow = 20, startRow = 11)
	@ScreenTableActions(actions = { @TableAction(actionValue = "2", displayName = "Change", defaultAction = true) })
	public static class WorkWithPrinterOutputRecord {

		@ScreenColumn(endColumn = 3, startColumn = 2, editable = true, selectionField = true)
		private String opt;

		@ScreenColumn(endColumn = 18, startColumn = 7, key = true)
		private String output;

		@ScreenColumn(endColumn = 66, startColumn = 21)
		private String status;
	}

	@ScreenField(row = 24, column = 2, fieldType = MessageField.class, endColumn = 37)
	private String message;

}
