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

@ScreenEntity(displayName = "Display Job Status Attributes")
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 26, value = "Display Job Status Attributes"), 
				@Identifier(row = 5, column = 2, value = "Status of job . . . . . . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class DisplayJobStatusAttributes1 {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(key = true, row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033329")
    private String number;
	
	@ScreenField(row = 5, column = 50, endColumn= 69 ,labelColumn= 2 ,displayName = "Status of job", sampleValue = "ACTIVE")
    private String statusOfJob;
	
	@ScreenField(row = 6, column = 50, endColumn= 59 ,labelColumn= 2 ,displayName = "Current user profile", sampleValue = "OPENLEGA1")
    private String currentUserProfile;
	
	@ScreenField(row = 7, column = 50, endColumn= 59 ,labelColumn= 2 ,displayName = "Job user identity", sampleValue = "OPENLEGA1")
    private String jobUserIdentity;
	
	@ScreenField(row = 8, column = 52, endColumn= 66 ,labelColumn= 4 ,displayName = "Set by", sampleValue = "*DEFAULT")
    private String setBy;
	
	@ScreenField(row = 10, column = 50, endColumn= 57 ,labelColumn= 4 ,displayName = "Date", sampleValue = "13/04/15")
    private String date1;
	
	@ScreenField(row = 11, column = 50, endColumn= 57 ,labelColumn= 4 ,displayName = "Time", sampleValue = "15:21:38")
    private String time1;
	
	@ScreenField(row = 13, column = 50, endColumn= 57 ,labelColumn= 4 ,displayName = "Date", sampleValue = "13/04/15")
    private String date;
	
	@ScreenField(row = 14, column = 50, endColumn= 57 ,labelColumn= 4 ,displayName = "Time", sampleValue = "15:21:38")
    private String time;
	
	@ScreenField(row = 15, column = 50, endColumn= 59 ,labelColumn= 2 ,displayName = "Subsystem", sampleValue = "QINTER")
    private String subsystem;
	
	@ScreenField(row = 16, column = 52, endColumn= 53 ,labelColumn= 4 ,displayName = "Subsystem pool ID", sampleValue = "2")
    private Integer subsystemPoolId;
	
	@ScreenField(row = 17, column = 50, endColumn= 59 ,labelColumn= 2 ,displayName = "Type of job", sampleValue = "INTER")
    private String typeOfJob;
	
	@ScreenField(row = 18, column = 50, endColumn= 54 ,labelColumn= 2 ,displayName = "Special environment", sampleValue = "*NONE")
    private String specialEnvironment;
	
	@ScreenField(row = 19, column = 50, endColumn= 51 ,labelColumn= 2 ,displayName = "Program return code", sampleValue = "1")
    private Integer programReturnCode;

	private DisplayJobStatusAttributes2 displayJobStatusAttributes2;


    


 
}
