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
				@Identifier(row = 1, column = 27, value = "Work with All Spooled Files") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F10.class, displayName = "View 4", alias = "view4"), 
				@Action(action = TerminalActions.F11.class, displayName = "View 2", alias = "view2"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F10.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Printers", alias = "printers"), 
				@Action(action = TerminalActions.F12.class ,additionalKey= AdditionalKey.SHIFT, displayName = "More keys", alias = "moreKeys") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "5") })
public class WorkWithAllSpooledFiles {
    
	
	@ScreenField(row = 21, column = 7, endColumn= 79 ,editable= true ,displayName = "Command")
    private String command;


    
    private List<WorkWithAllSpooledFilesRecord> workWithAllSpooledFilesRecords;


	@ScreenTable(startRow = 10, endRow = 18)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "1", displayName = "Send", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "send"),
			@TableAction(actionValue = "2", displayName = "Change", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "change"),
			@TableAction(actionValue = "3", displayName = "Hold", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "hold"),
			@TableAction(actionValue = "4", displayName = "Delete", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "delete"),
			@TableAction(actionValue = "5", displayName = "Display", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "display"),
			@TableAction(actionValue = "6", displayName = "Release", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "release"),
			@TableAction(actionValue = "7", displayName = "Messages", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "messages"),
			@TableAction(actionValue = "8", displayName = "Attributes", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "attributes"),
			@TableAction(actionValue = "9", displayName = "Work with printing status", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "workWithPrintingStatus") })
	public static class WorkWithAllSpooledFilesRecord {
		@ScreenColumn(startColumn = 7, endColumn = 16, key=true ,displayName = "File", sampleValue="QPJOBLOG")
		private String file;
		@ScreenColumn(startColumn = 19, endColumn = 28 ,displayName = "User", sampleValue="OPENLEGA1")
		private String user;
		@ScreenColumn(startColumn = 31, endColumn = 40 ,displayName = "Queue", sampleValue="QEZJOBLOG")
		private String queue;
		@ScreenColumn(startColumn = 43, endColumn = 52 ,displayName = "User Data", sampleValue="QPADEV007R")
		private String userData;
		@ScreenColumn(startColumn = 55, endColumn = 58 ,displayName = "Sts", sampleValue="RDY")
		private String sts;
		@ScreenColumn(startColumn = 61, endColumn = 66 ,displayName = "Pages", sampleValue="6")
		private Integer pages;
		@ScreenColumn(startColumn = 77, endColumn = 79, mainDisplayField=true ,displayName = "Copy", sampleValue="1")
		private Integer copy;
		@ScreenColumn(startColumn = 69, endColumn = 73, displayName = "")
		private String newColumn;
		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, selectionField = true, displayName = "Opt")
		private String opt;
		
		
	}    
 
}
