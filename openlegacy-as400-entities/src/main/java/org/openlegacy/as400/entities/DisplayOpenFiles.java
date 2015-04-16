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
				@Identifier(row = 1, column = 31, value = "Display Open Files         ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "14") })
public class DisplayOpenFiles {
    
	
	@ScreenField(row = 3, column = 14, endColumn= 23 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 42, endColumn= 51 ,labelColumn= 29 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 74, endColumn= 79 ,labelColumn= 57 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(endColumn = 55, row = 4, column = 52, labelColumn = 2, sampleValue = "2", endRow = 4, displayName = "Number of open data paths")
	private String numberOfOpenDataPaths;

	private List<DisplayOpenFilesRecord> displayOpenFilesRecords;

	@ScreenTable(startRow = 9, endRow = 19)
	public static class DisplayOpenFilesRecord {
		@ScreenColumn(startColumn = 2, endColumn = 11, displayName = "File")
		private String file;
		@ScreenColumn(startColumn = 13, endColumn = 22, displayName = "Library")
		private String library;
		@ScreenColumn(startColumn = 24, endColumn = 33, displayName = "Device", key = true)
		private String device;
		@ScreenColumn(startColumn = 36, endColumn = 45, displayName = "Scope")
		private String scope;
		@ScreenColumn(startColumn = 48, endColumn = 57, displayName = "Activation")
		private String activation;
		@ScreenColumn(startColumn = 59, endColumn = 74, displayName = "Group")
		private String group;
	}


    


 
}
