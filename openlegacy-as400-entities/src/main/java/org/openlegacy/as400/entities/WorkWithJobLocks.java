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
				@Identifier(row = 1, column = 31, value = "Work with Job Locks                  "), 
				@Identifier(row = 5, column = 2, value = "Job status:") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Display job record locks", alias = "displayJobRecordLocks"), 
				@Action(action = TerminalActions.F11.class, displayName = "Display thread data", alias = "displayThreadData"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "12") })
public class WorkWithJobLocks {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(row = 5, column = 16, endColumn= 22 ,labelColumn= 2 ,displayName = "Job status", sampleValue = "ACTIVE")
    private String jobStatus;


    
    private List<WorkWithJobLocksRecord> workWithJobLocksRecords;


	@ScreenTable(startRow = 12, endRow = 20)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "5", displayName = "Work with job member locks", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "workWithJobMemberLocks"),
			@TableAction(actionValue = "8", displayName = "Work with object locks", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "workWithObjectLocks") })
	public static class WorkWithJobLocksRecord {
		@ScreenColumn(startColumn = 7, endColumn = 16, key=true ,displayName = "Object", sampleValue="GAMES400")
		private String object;
		@ScreenColumn(startColumn = 19, endColumn = 28 ,displayName = "Library", sampleValue="QSYS")
		private String library;
		@ScreenColumn(startColumn = 52, endColumn = 55, mainDisplayField=true ,displayName = "Status", sampleValue="HELD")
		private String status;
		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, selectionField = true, displayName = "Opt")
		private String opt;
		@ScreenColumn(startColumn = 31, endColumn = 39, displayName = "Type")
		private String type;
		@ScreenColumn(startColumn = 42, endColumn = 48, displayName = "Lock")
		private String lock;
		@ScreenColumn(startColumn = 60, endColumn = 63, displayName = "Locks")
		private String locks;
		@ScreenColumn(startColumn = 67, endColumn = 76, displayName = "Device")
		private String device;
		
		
	}    
 
}
