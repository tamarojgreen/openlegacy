package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 2, value = "                             Programmer Menu                                 "), 
				@Identifier(row = 15, column = 2, value = "Selection . . . . ."), 
				@Identifier(row = 15, column = 38, value = "Parm . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F6.class, displayName = "Display messages", alias = "displayMessages"), 
				@Action(action = TerminalActions.F10.class, displayName = "Command entry", alias = "commandEntry"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Work with submitted jobs", alias = "workWithSubmittedJobs"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Work with output ", alias = "workWithOutput") 
				})
@ScreenNavigation(accessedFrom = Programming.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class ProgrammerMenu {
    
	
	@ScreenField(row = 2, column = 72, endColumn= 79 ,labelColumn= 56 ,displayName = "System", sampleValue = "S1051C6E")
    private String system;
	
	@ScreenField(row = 15, column = 24, endColumn= 25 ,labelColumn= 2 ,editable= true ,displayName = "Selection")
    private Integer selection;
	
	@ScreenField(row = 15, column = 53, endColumn= 62 ,labelColumn= 38 ,editable= true ,displayName = "Parm")
    private String parm;
	
	@ScreenField(row = 16, column = 24, endColumn= 33 ,labelColumn= 2 ,editable= true ,displayName = "Type")
    private String type;
	
	@ScreenField(row = 16, column = 53, endColumn= 73 ,labelColumn= 38 ,editable= true ,displayName = "Parm 2")
    private String parm2;
	
	@ScreenField(row = 17, column = 24, endColumn= 80 ,labelColumn= 2 ,editable= true ,displayName = "Command")
    private String command;
	
	@ScreenField(row = 19, column = 24, endColumn= 33 ,labelColumn= 2 ,editable= true ,displayName = "Source file")
    private String sourceFile;
	
	@ScreenField(row = 19, column = 69, endColumn= 78 ,labelColumn= 38 ,editable= true ,displayName = "Source library", sampleValue = "*LIBL")
    private String sourceLibrary;
	
	@ScreenField(row = 20, column = 24, endColumn= 33 ,labelColumn= 2 ,editable= true ,displayName = "Object library")
    private String objectLibrary;
	
	@ScreenField(row = 20, column = 69, endColumn= 78 ,labelColumn= 38 ,editable= true ,displayName = "Job description", sampleValue = "*USRPRF")
    private String jobDescription;

    


 
}
