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
				@Identifier(row = 5, column = 2, value = "Job switches  . . . . . . . . . . . . . . . . . . :"), 
				@Identifier(row = 6, column = 2, value = "Inquiry message reply . . . . . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Change job", alias = "changeJob"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = DisplayJobDefinitionAttributes2.class, terminalAction = PAGE_DOWN.class, exitAction = F12.class)
public class DisplayJobDefinitionAttributes3 {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 5, column = 56, endColumn= 63)
    private String jobSwitches;
	
	@ScreenField(row = 6, column = 56, endColumn= 63)
    private String inquiryMessageReplay;
	
	@ScreenField(row = 7, column = 56, endColumn= 70)
    private String accountingCode;
	
	@ScreenField(row = 8, column = 56, endColumn= 80)
    private String printerText;
	
	@ScreenField(row = 10, column = 56, endColumn= 60)
    private String domConversation;
	
	@ScreenField(row = 11, column = 56, endColumn= 62)
    private String breakMessageHandling;
	
	@ScreenField(row = 12, column = 56, endColumn= 62)
    private String statusMessage;
	
	@ScreenField(row = 13, column = 56, endColumn= 68)
    private String deviceRecoveryAction;
	
	@ScreenField(row = 14, column = 56, endColumn= 65)
    private String timeSliceEndPool;
	
	@ScreenField(row = 15, column = 56, endColumn= 65)
    private String printKeyFormat;
	
	@ScreenField(row = 16, column = 56, endColumn= 65)
    private String sortSequence;
	
	@ScreenField(row = 17, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Library")
    private String sortSequenceLibary;
	
	@ScreenField(row = 18, column = 56, endColumn= 58)
    private String languageIdentifier;
	
	@ScreenField(row = 19, column = 56, endColumn= 57)
    private String countryOrRegionIdentifier;

	@ScreenField(endColumn = 62, row = 3, labelColumn = 47, column = 57, sampleValue = "033330", endRow = 3, displayName = "Number")
	private String number;


    


 
}
