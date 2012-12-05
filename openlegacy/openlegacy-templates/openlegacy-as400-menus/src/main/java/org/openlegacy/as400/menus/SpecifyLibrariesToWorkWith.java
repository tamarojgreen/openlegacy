package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity(child=true)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 2, value = "                       Specify Libraries to Work With                          "), 
				@Identifier(row = 5, column = 4, value = "Library  . . . . . . . . . . ."), 
				@Identifier(row = 7, column = 4, value = "ASP number . . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = ProgrammingDevelopmentManagerpdm.class 
)
public class SpecifyLibrariesToWorkWith {
    
	
	@ScreenField(row = 5, column = 37, endColumn= 46 ,labelColumn= 4 ,editable= true ,displayName = "Library", sampleValue = "*LIBL")
    private String library;
	
	@ScreenField(row = 7, column = 37, endColumn= 42 ,labelColumn= 4 ,editable= true ,displayName = "ASP number", sampleValue = "*ALL")
    private String aspNumber;
	
	@ScreenField(row = 8, column = 37, endColumn= 46 ,labelColumn= 4 ,editable= true ,displayName = "ASP device", sampleValue = "*")
    private String aspDevice;

    


 
}
