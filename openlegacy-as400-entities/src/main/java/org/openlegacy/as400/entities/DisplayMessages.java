package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity(window = true, screenType = MessagesEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 33, value = "Display Messages        ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F11.class, displayName = "Remove a message", alias = "removeAMessage"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F1.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Remove all", alias = "removeAll"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Remove all except unanswered", alias = "removeAllExceptUnanswered"), 
				@Action(action = TerminalActions.F12.class ,additionalKey= AdditionalKey.SHIFT, displayName = "More keys", alias = "moreKeys") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "2") })
public class DisplayMessages {
    
	
	@ScreenField(row = 5, column = 66, endColumn= 75 ,labelColumn= 46 ,displayName = "Delivery", sampleValue = "*BREAK")
    private String delivery;

	@ScreenField(endColumn = 23, row = 5, column = 22, labelColumn = 2, sampleValue = "00", endRow = 5, displayName = "Severity")
	private String severity;

	@ScreenField(row = 8, endRow = 20, column = 4, endColumn = 77, rectangle = true, sampleValue = "TEST                                                                      ", fieldType = MessageField.class)
	private String message;


    


 
}
