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
				@Identifier(row = 1, column = 31, value = "Display Library List                 "), 
				@Identifier(row = 5, column = 2, value = "Type options, press Enter."), 
				@Identifier(row = 8, column = 29, value = "ASP") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu"), 
				@Action(action = TerminalActions.F5.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Top", alias = "top"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Bottom", alias = "bottom") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "13") })
public class DisplayLibraryList {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033328")
    private String number;


    
    private List<DisplayLibraryListRecord> displayLibraryListRecords;


	@ScreenTable(startRow = 10, endRow = 21)
	@ScreenTableActions(actions = { @TableAction(actionValue = "5", displayName = "Display objects in libary", targetEntity = NONE.class, defaultAction = false, action = ENTER.class, alias = "displayObjectsInLibary") })
	public static class DisplayLibraryListRecord {
		@ScreenColumn(startColumn = 7, endColumn = 16, key=true ,displayName = "Library", sampleValue="QSYS")
		private String library;
		@ScreenColumn(startColumn = 19, endColumn = 26 ,displayName = "Type", sampleValue="SYS")
		private String type;
		@ScreenColumn(startColumn = 41, endColumn = 79, mainDisplayField=true ,displayName = "Text", sampleValue="System Library")
		private String text;
		@ScreenColumn(startColumn = 3, endColumn = 3, editable = true, selectionField = true, displayName = "Opt")
		private String opt;
		@ScreenColumn(startColumn = 29, endColumn = 38, displayName = "Device")
		private String device;
		
		
	}    
 
}
