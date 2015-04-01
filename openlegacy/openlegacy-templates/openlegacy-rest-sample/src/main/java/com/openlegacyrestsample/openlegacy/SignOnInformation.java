package com.openlegacyrestsample.openlegacy;

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
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 31, value = "Sign-on Information") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F9.class, displayName = "Change password", alias = "changePassword") 
				})
public class SignOnInformation {
    
	
	@ScreenField(row = 3, column = 49, endColumn = 66, fieldType = MessageField.class)
    private String previousSignOn;


    


 
}
