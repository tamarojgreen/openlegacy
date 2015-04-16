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
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 32, value = "Display Call Stack") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F11.class, displayName = "Display activation group", alias = "displayActivationGroup"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu"), 
				@Action(action = TerminalActions.F5.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Top", alias = "top"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Bottom", alias = "bottom"), 
				@Action(action = TerminalActions.F10.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Display entire field", alias = "displayEntireField") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "11") })
public class DisplayCallStack {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(endColumn = 19, row = 4, column = 12, labelColumn = 2, sampleValue = "00000164", endRow = 4, displayName = "Thread")
	private String thread;

	private List<DisplayCallStackRecord> displayCallStackRecords;

	@ScreenTable(startRow = 8, endRow = 20)
	public static class DisplayCallStackRecord {
		@ScreenColumn(startColumn = 2, endColumn = 5, displayName = "Type")
		private String type;
		@ScreenColumn(startColumn = 8, endColumn = 28, key = true, displayName = "Program")
		private String program;
		@ScreenColumn(startColumn = 33, endColumn = 48, displayName = "Statement")
		private String statement;
		@ScreenColumn(startColumn = 51, endColumn = 77, displayName = "Procedure")
		private String procedure;
	}


    


 
}
