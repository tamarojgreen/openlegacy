package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 33, value = "Hold Job (HLDJOB)             "), 
				@Identifier(row = 5, column = 2, value = "Job name . . . . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "42") })
public class HoldJobhldjob {
    
	
	@ScreenField(row = 5, column = 37, endColumn= 46 ,labelColumn= 2 ,displayName = "Job name", sampleValue = "QPADEV007R")
    private String jobName;
	
	@ScreenField(row = 6, column = 39, endColumn= 47 ,labelColumn= 2 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(row = 7, column = 39, endColumn= 44 ,labelColumn= 2 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenBooleanField(trueValue = "*YES", falseValue = "*NO")
	@ScreenField(endColumn = 40, row = 8, column = 37, labelColumn = 2, sampleValue = "*NO", editable = true, endRow = 8, displayName = "Hold spooled files")
	private Boolean holdSpooledFiles;


    


 
}
