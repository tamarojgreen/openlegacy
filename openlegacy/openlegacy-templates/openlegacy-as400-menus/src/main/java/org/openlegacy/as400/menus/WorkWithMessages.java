package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.modules.messages.Messages.MessagesEntity;  
import org.openlegacy.modules.messages.Messages.MessageField;  

@ScreenEntity(screenType=MessagesEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 32, value = "Work with Messages"), 
				@Identifier(row = 8, column = 2, value = "Opt"), 
				@Identifier(row = 8, column = 8, value = "Message") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F6.class, displayName = "Display system operator messages", alias = "displaySystemOperatorMessages"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Remove messages not needing a reply", alias = "removeMessagesNotNeedingAReply"), 
				@Action(action = TerminalActions.F5.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Top", alias = "top"), 
				@Action(action = TerminalActions.F12.class ,additionalKey= AdditionalKey.SHIFT, displayName = "More keys", alias = "moreKeys") 
				})
public class WorkWithMessages {
    
	
	@ScreenField(row = 2, column = 72, endColumn= 79 ,labelColumn= 62 ,displayName = "System", sampleValue = "S1051C6E")
    private String system;
	
	@ScreenField(row = 3, column = 18, endColumn= 27 ,labelColumn= 2 ,displayName = "Messages for", sampleValue = "RMR20924")
    private String messagesFor;
	
	@ScreenField(row = 5, column = 2, endColumn= 79 ,endRow= 13 ,rectangle= true ,fieldType=MessageField.class ,displayName = "Message", sampleValue = "Type options below, then press Enter.")
    private String message;

    


 
}
