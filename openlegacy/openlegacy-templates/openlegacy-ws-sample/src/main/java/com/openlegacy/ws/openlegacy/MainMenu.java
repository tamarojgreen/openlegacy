package com.openlegacy.ws.openlegacy;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.modules.menu.Menu.MenuEntity;  

@ScreenEntity(screenType=MenuEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 33, value = "i5/OS Main Menu") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F1.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Information Assistant", alias = "informationAssistant"), 
				@Action(action = TerminalActions.F11.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Set initial menu", alias = "setInitialMenu") 
				})
public class MainMenu {
    
	
	@ScreenField(row = 20, column = 7, endColumn= 85 ,editable= true ,displayName = "")
    private String menuSelection;


    


 
}
