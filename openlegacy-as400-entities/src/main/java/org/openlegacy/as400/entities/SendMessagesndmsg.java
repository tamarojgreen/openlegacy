package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 31, value = "Send Message (SNDMSG)           "), 
				@Identifier(row = 5, column = 2, value = "Message text . . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "3") })
public class SendMessagesndmsg {
    
	
	@ScreenField(row = 5, column = 37, endColumn = 80, editable = true, displayName = "Command")
    private String messageText;
	
	@ScreenField(row = 12, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "To user profile", helpText = "Name, *SYSOPR, *ALLACT...")
    private String toUserProfile;


    


 
}
