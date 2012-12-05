package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.modules.messages.Messages.MessagesEntity;  
import org.openlegacy.modules.messages.Messages.MessageField;  

@ScreenEntity(screenType=MessagesEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 25, value = "        Display Messages        "), 
				@Identifier(row = 4, column = 4, value = "Library . . . :"), 
				@Identifier(row = 4, column = 48, value = "Library . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F11.class, displayName = "Remove a message", alias = "removeAMessage"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F1.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Remove all", alias = "removeAll"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Remove all except unanswered", alias = "removeAllExceptUnanswered"), 
				@Action(action = TerminalActions.F12.class ,additionalKey= AdditionalKey.SHIFT, displayName = "More keys", alias = "moreKeys") 
				})
public class DisplayMessages {
    
	
	@ScreenField(row = 2, column = 66, endColumn= 73 ,labelColumn= 50 ,displayName = "System", sampleValue = "S1051C6E")
    private String system;
	
	@ScreenField(row = 3, column = 22, endColumn= 31 ,labelColumn= 2 ,displayName = "Queue", sampleValue = "RMR20924")
    private String queue;
	
	@ScreenField(row = 3, column = 66, endColumn= 75 ,labelColumn= 46 ,displayName = "Program", sampleValue = "*DSPMSG")
    private String program;
	
	@ScreenField(row = 4, column = 24, endColumn= 33 ,labelColumn= 4 ,displayName = "Library", sampleValue = "QUSRSYS")
    private String library1;
	
	@ScreenField(row = 4, column = 68, endColumn= 77 ,labelColumn= 48 ,displayName = "Library")
    private String library;
	
	@ScreenField(row = 5, column = 22, endColumn= 23 ,labelColumn= 2 ,displayName = "Severity", sampleValue = "00")
    private Integer severity;
	
	@ScreenField(row = 5, column = 66, endColumn= 75 ,labelColumn= 46 ,displayName = "Delivery", sampleValue = "*NOTIFY")
    private String delivery;
	
	@ScreenField(row = 7, column = 2, endColumn= 77 ,endRow= 15 ,rectangle= true ,fieldType=MessageField.class ,displayName = "Message", sampleValue = "Type reply (if required), press Enter.")
    private String message;

    


 
}
