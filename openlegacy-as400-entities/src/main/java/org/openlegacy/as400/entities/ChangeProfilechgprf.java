package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 30, value = "Change Profile (CHGPRF)          "), 
				@Identifier(row = 5, column = 2, value = "Assistance level . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "9") })
public class ChangeProfilechgprf {
    
	
	@ScreenField(row = 5, column = 37, endColumn= 45 ,labelColumn= 2 ,editable= true ,displayName = "Assistance level", sampleValue = "*INTERMED", helpText = "*SAME, *SYSVAL, *BASIC...")
    private String assistanceLevel;
	
	@ScreenField(row = 6, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Current library", sampleValue = "OPENLEGA11", helpText = "Name, *SAME, *CRTDFT")
    private String currentLibrary;
	
	@ScreenField(row = 7, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Initial program to call", sampleValue = "SHOWNEWS2", helpText = "Name, *SAME, *NONE")
    private String initialProgramToCall;
	
	@ScreenField(row = 8, column = 39, endColumn= 48 ,labelColumn= 2 ,editable= true ,displayName = "Library", sampleValue = "PGMLIBNEU", helpText = "Name, *LIBL, *CURLIB")
    private String initialProgramToCallLibary;
	
	@ScreenField(row = 9, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Initial menu", sampleValue = "MAIN", helpText = "Name, *SAME, *SIGNOFF")
    private String initialMenu;
	
	@ScreenField(row = 10, column = 39, endColumn= 48 ,labelColumn= 2 ,editable= true ,displayName = "Library", sampleValue = "*LIBL", helpText = "Name, *LIBL, *CURLIB")
    private String initialMenuLibary;
	
	@ScreenField(row = 11, column = 37, endColumn = 80, editable = true, displayName = "Command", sampleValue = "'GRPOL: Zeev Avidan - OpenLegacy'")
    private String decription;


    


 
}
