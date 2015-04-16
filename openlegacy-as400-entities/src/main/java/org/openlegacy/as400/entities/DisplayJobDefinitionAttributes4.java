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
				@Identifier(row = 5, column = 2, value = "Coded character set identifier  . . . . . . . . . :"), 
				@Identifier(row = 6, column = 2, value = "Default coded character set identifier  . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Change job", alias = "changeJob"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = DisplayJobDefinitionAttributes3.class, terminalAction = PAGE_DOWN.class, exitAction = F12.class)
public class DisplayJobDefinitionAttributes4 {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033330")
    private String number;
	
	@ScreenField(row = 5, column = 56, endColumn= 65)
    private String codedCharacterSetIdentifier;
	
	@ScreenField(row = 6, column = 56, endColumn= 65)
    private String defaultCodedCharacterSetIdentifier;
	
	@ScreenField(row = 7, column = 56, endColumn= 65)
    private String characterIdentifierControl;
	
	@ScreenField(row = 8, column = 56, endColumn= 61)
    private String jobMessageQueueMaximumSize;
	
	@ScreenField(row = 9, column = 56, endColumn= 65)
    private String jobMessageQueueFullAction;
	
	@ScreenField(row = 10, column = 56, endColumn= 59)
    private String allowMultipleThreads;
	
	@ScreenField(row = 11, column = 56, endColumn= 65)
    private String auxiliaryStoragePoolGroup;
	
	@ScreenField(row = 12, column = 56, endColumn= 65)
    private String spooledFileAction;


    


 
}
