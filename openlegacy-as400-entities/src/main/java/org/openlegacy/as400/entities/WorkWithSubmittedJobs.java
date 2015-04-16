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

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 29, value = "Work with Submitted Jobs"), 
				@Identifier(row = 3, column = 2, value = "Submitted from . . . . . . . . :") 
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
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "6") })
public class WorkWithSubmittedJobs {
    
	
	@ScreenField(row = 3, column = 37, endColumn= 46 ,labelColumn= 2 ,displayName = "Submitted from", sampleValue = "*USER")
    private String submittedFrom;
	
	@ScreenField(row = 21, column = 7, endColumn= 79 ,editable= true ,displayName = "Command")
    private String command;


    


 
}
