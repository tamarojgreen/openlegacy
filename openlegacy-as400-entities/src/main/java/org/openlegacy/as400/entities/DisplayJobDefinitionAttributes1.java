package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity(displayName = "Display Job Definition Attributes")
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 24, value = "Display Job Definition Attributes           "), 
				@Identifier(row = 5, column = 2, value = "Job description . . . . . . . . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Change job", alias = "changeJob"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "2") })
public class DisplayJobDefinitionAttributes1 {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 5, column = 56, endColumn= 65)
    private String jobDescription;
	
	@ScreenField(row = 6, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Library", sampleValue = "QGPL")
    private String jobDescriptionLibary;
	
	@ScreenField(row = 7, column = 56, endColumn= 65)
    private String jobQueue;
	
	@ScreenField(row = 8, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Library")
    private String jobQueueLibary;
	
	@ScreenField(row = 9, column = 56, endColumn= 56)
    private String jobPriority;
	
	@ScreenField(row = 10, column = 56, endColumn= 56)
    private String outputPriority;
	
	@ScreenField(row = 11, column = 56, endColumn= 65)
    private String endSeverity;
	
	@ScreenField(row = 15, column = 56, endColumn= 62 ,labelColumn= 4 ,displayName = "Text", sampleValue = "*SECLVL")
    private String text;
	
	@ScreenField(row = 16, column = 56, endColumn= 59)
    private String logClProgramCommands;
	
	@ScreenField(row = 17, column = 56, endColumn= 65)
    private String jobLogOutput;
	
	@ScreenField(row = 18, column = 56, endColumn= 65)
    private String printerDevice;

	@ScreenField(key = true, row = 3, column = 57, endColumn = 62, labelColumn = 47, sampleValue = "033328", endRow = 3, displayName = "Number")
	private String number;

	@ScreenField(endColumn = 65, row = 14, labelColumn = 4, column = 56, sampleValue = "0", endRow = 14, displayName = "Severity")
	private String severity;

	@ScreenField(endColumn = 56, row = 13, column = 56, labelColumn = 4, sampleValue = "4", endRow = 13, displayName = "Level")
	private String level;

	private DisplayJobDefinitionAttributes2 displayJobDefinitionAttributes2;

	private DisplayJobDefinitionAttributes3 displayJobDefinitionAttributes3;

	private DisplayJobDefinitionAttributes4 displayJobDefinitionAttributes4;


    


 
}
