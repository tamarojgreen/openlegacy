package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 29, value = "Work with Printer Output"), 
				@Identifier(row = 5, column = 2, value = "Type options below, then press Enter.  To work with printers, press F22.      "), 
				@Identifier(row = 9, column = 7, value = "Printer/") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Command line", alias = "commandLine"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F9.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Select assistance level", alias = "selectAssistanceLevel"), 
				@Action(action = TerminalActions.F10.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Work with printers", alias = "workWithPrinters") 
				})
public class WorkWithPrinterOutput {
    
	
	@ScreenField(row = 2, column = 72, endColumn= 79 ,labelColumn= 62 ,displayName = "System", sampleValue = "S1051C6E")
    private String system;
	
	@ScreenField(row = 3, column = 21, endColumn= 30 ,labelColumn= 2 ,displayName = "User", sampleValue = "RMR20924")
    private String user;

    


 
}
