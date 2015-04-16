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
				@Identifier(row = 1, column = 26, value = "Display Communications Status") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F11.class, displayName = "Display operations", alias = "displayOperations"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu"), 
				@Action(action = TerminalActions.F5.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Top", alias = "top"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Bottom", alias = "bottom") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "17") })
public class DisplayCommunicationsStatus {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033328")
    private String number;

	private List<DisplayCommunicationsStatusRecord> displayCommunicationsStatusRecords;

	@ScreenTable(startRow = 10, endRow = 11, name = "Output")
	@ScreenTableActions(actions = { @TableAction(actionValue = "5", displayName = "Display", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "display") })
	public static class DisplayCommunicationsStatusRecord {
		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, selectionField = true, displayName = "Opt")
		private String opt;
		@ScreenColumn(startColumn = 7, endColumn = 22, key = true, displayName = "Identifier")
		private String identifier;
		@ScreenColumn(startColumn = 25, endColumn = 38, displayName = "Method")
		private String method;
	}


    


 
}
