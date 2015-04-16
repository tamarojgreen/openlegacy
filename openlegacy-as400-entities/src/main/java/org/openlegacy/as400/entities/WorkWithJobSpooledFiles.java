package org.openlegacy.as400.entities;

import java.util.List;

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
import org.openlegacy.terminal.ScreenEntity.NONE;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 27, value = "Work with Job Spooled Files"), 
				@Identifier(row = 5, column = 2, value = "Type options, press Enter.") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F10.class, displayName = "View 3", alias = "view3"), 
				@Action(action = TerminalActions.F11.class, displayName = "View 2", alias = "view2"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F10.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Printers", alias = "printers"), 
				@Action(action = TerminalActions.F12.class ,additionalKey= AdditionalKey.SHIFT, displayName = "More keys", alias = "moreKeys") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "4") })
public class WorkWithJobSpooledFiles {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 22, column = 7, endColumn= 79 ,editable= true ,displayName = "Command")
    private String command;

	@ScreenField(endColumn = 62, row = 3, labelColumn = 47, column = 57, sampleValue = "033328", endRow = 3, displayName = "Number")
	private String number;

	private List<WorkWithJobSpooledFilesRecord> workWithJobSpooledFilesRecords;

	@ScreenTable(startRow = 11, endRow = 20)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "1", displayName = "Send", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "send"),
			@TableAction(actionValue = "2", displayName = "change", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "Change"),
			@TableAction(actionValue = "3", displayName = "Hold", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "hold"),
			@TableAction(actionValue = "4", displayName = "Delete", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "delete"),
			@TableAction(actionValue = "5", displayName = "Display", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "display"),
			@TableAction(actionValue = "6", displayName = "Release", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "release"),
			@TableAction(actionValue = "7", displayName = "Messages", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "messages"),
			@TableAction(actionValue = "8", displayName = "Attributes", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "attributes"),
			@TableAction(actionValue = "9", displayName = "Work with printing status", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "workWithPrintingStatus") })
	public static class WorkWithJobSpooledFilesRecord {
		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, selectionField = true, displayName = "Opt")
		private String opt;
		@ScreenColumn(startColumn = 19, endColumn = 28, displayName = "Queue")
		private String queue;
		@ScreenColumn(startColumn = 7, endColumn = 16, displayName = "File", key = true)
		private String file;
		@ScreenColumn(startColumn = 31, endColumn = 40, displayName = "User Data")
		private String userData;
		@ScreenColumn(startColumn = 44, endColumn = 47, displayName = "Status")
		private String status;
		@ScreenColumn(startColumn = 51, endColumn = 56, displayName = "Pages")
		private String pages;
		@ScreenColumn(startColumn = 60, endColumn = 64, displayName = "")
		private String newColumn;
		@ScreenColumn(startColumn = 70, endColumn = 72, displayName = "Copies")
		private String copies;
	}


    


 
}
