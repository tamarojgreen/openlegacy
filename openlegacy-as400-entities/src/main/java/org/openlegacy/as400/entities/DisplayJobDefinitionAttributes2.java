package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;
import org.openlegacy.terminal.actions.TerminalActions.PAGE_DOWN;

@ScreenEntity(displayName = "Display Job Definition Attributes")
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 24, value = "Display Job Definition Attributes           "), 
				@Identifier(row = 5, column = 2, value = "Default output queue  . . . . . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Change job", alias = "changeJob"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = DisplayJobDefinitionAttributes1.class, terminalAction = PAGE_DOWN.class, exitAction = F12.class)
public class DisplayJobDefinitionAttributes2 {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(key = true, row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033330")
    private Integer number;
	
	@ScreenField(row = 5, column = 56, endColumn= 65)
    private String defaultOutputQueue;
	
	@ScreenField(row = 6, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Library", sampleValue = "QGPL")
    private String defaultOutputQueueLibary;
	
	@ScreenField(row = 7, column = 56, endColumn= 65)
    private String jobDate;
	
	@ScreenField(row = 8, column = 56, endColumn= 59)
    private String dateFormat;
	
	@ScreenField(row = 9, column = 56, endColumn= 61)
    private String dateSeparator;
	
	@ScreenField(row = 10, column = 56, endColumn= 61)
    private String timeSeparator;
	
	@ScreenField(row = 12, column = 56, endColumn= 65 ,labelColumn= 4 ,displayName = "Current date", sampleValue = "13/04/2015")
    private String currentDate;
	
	@ScreenField(row = 13, column = 56, endColumn= 63 ,labelColumn= 4 ,displayName = "Current time", sampleValue = "15:29:16")
    private String currentTime;
	
	@ScreenField(row = 14, column = 56, endColumn= 65)
    private String timeZone;
	
	@ScreenField(row = 15, column = 58, endColumn= 63)
    private String currentOffset;
	
	@ScreenField(row = 16, column = 58, endColumn= 80 ,labelColumn= 4 ,displayName = "Full name", sampleValue = "Central European Daylig")
    private String fullName;
	
	@ScreenField(row = 18, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Abbreviated name", sampleValue = "CEST")
    private String abbreviatedName;
	
	@ScreenField(row = 19, column = 56, endColumn= 61)
    private String decimalFormat;


    


 
}
