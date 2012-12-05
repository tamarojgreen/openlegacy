package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 2, value = "                        Programming Development Manager (PDM)                 "), 
				@Identifier(row = 15, column = 7, value = "Information about new tools - press F1 for details                        "), 
				@Identifier(row = 19, column = 2, value = "Selection or command                                                          ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"), 
				@Action(action = TerminalActions.F10.class, displayName = "Command entry", alias = "commandEntry"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Change defaults", alias = "changeDefaults") 
				})
public class ProgrammingDevelopmentManagerpdm {
    

    private SpecifyLibrariesToWorkWith specifyLibrariesToWorkWith;
    


 
}
