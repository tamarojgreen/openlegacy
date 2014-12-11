package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.modules.menu.Menu.MenuEntity;  
import org.openlegacy.modules.menu.Menu.MenuSelectionField;  

@ScreenEntity(screenType=MenuEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 34, value = "Work with Job"), 
				@Identifier(row = 5, column = 2, value = "Select one of the following:"), 
				@Identifier(row = 19, column = 68, value = "     More...") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = UserTasks.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class WorkWithJob {
    
	
	@ScreenField(row = 2, column = 72, endColumn= 79 ,labelColumn= 62 ,displayName = "System", sampleValue = "S1051C6E")
    private String system;
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV0001")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "RMR20924")
    private String user;
	
	@ScreenField(row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "698775")
    private Integer number;
	
	@ScreenField(row = 21, column = 7, endColumn= 80 ,editable= true ,fieldType=MenuSelectionField.class ,displayName = "Menu Selection")
    private String menuSelection;

    


 
}
