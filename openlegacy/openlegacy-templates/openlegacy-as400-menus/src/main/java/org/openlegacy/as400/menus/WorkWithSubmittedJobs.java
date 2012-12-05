package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 29, value = "Work with Submitted Jobs"), 
				@Identifier(row = 5, column = 2, value = "Type options, press Enter."), 
				@Identifier(row = 10, column = 2, value = "Opt") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"), 
				@Action(action = TerminalActions.F11.class, displayName = "Display schedule data", alias = "displayScheduleData"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F5.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Top", alias = "top"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Bottom", alias = "bottom") 
				})
public class WorkWithSubmittedJobs {
    
	
	@ScreenField(row = 3, column = 37, endColumn= 46 ,labelColumn= 2 ,displayName = "Submitted from", sampleValue = "*USER")
    private String submittedFrom;

    


 
}
