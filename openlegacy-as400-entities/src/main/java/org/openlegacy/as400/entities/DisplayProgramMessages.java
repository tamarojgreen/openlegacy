package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity(window = true, screenType = MessagesEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 16, value = "            Display Program Messages            ") })
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit") 
				})
public class DisplayProgramMessages {
    
	
	@ScreenField(row = 3, column = 2, endColumn = 77, endRow = 4, fieldType = MessageField.class)
    private String message;


    


 
}
