package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 2, value = "                           Select Definition Type                              "), 
				@Identifier(row = 5, column = 2, value = "  Definition type . . . . . . ."), 
				@Identifier(row = 9, column = 2, value = "  Data dictionary . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F10.class, displayName = "Work with database files", alias = "workWithDatabaseFiles"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F10.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Work with data dictionaries", alias = "workWithDataDictionaries") 
				})
@ScreenNavigation(accessedFrom = InteractiveDataDefinitionUtility.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class SelectDefinitionType {
    
	
	@ScreenField(row = 5, column = 36, endColumn= 36 ,labelColumn= 2 ,editable= true ,displayName = "Definition type")
    private String definitionType;
	
	@ScreenField(row = 9, column = 36, endColumn= 45 ,labelColumn= 2 ,editable= true ,displayName = "Data dictionary")
    private String dataDictionary;

    


 
}
