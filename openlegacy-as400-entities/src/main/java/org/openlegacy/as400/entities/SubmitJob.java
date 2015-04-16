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
				@Identifier(row = 1, column = 32, value = "Submit Job (SBMJOB)            "), 
				@Identifier(row = 5, column = 2, value = "Command to run . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "4") })
public class SubmitJob {
    
	
	@ScreenField(row = 5, column = 37, endColumn = 80, editable = true, displayName = "Command")
    private String command;
	
	@ScreenField(row = 12, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Job name", sampleValue = "*JOBD", helpText = "Name, *JOBD")
    private String jobName;
	
	@ScreenField(row = 13, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Job description", sampleValue = "*USRPRF", helpText = "Name, *USRPRF")
    private String jobDescription;
	
	@ScreenField(row = 14, column = 39, endColumn= 48 ,labelColumn= 2 ,editable= true ,displayName = "Library", helpText = "Name, *LIBL, *CURLIB")
    private String jobDescriptionLibary;
	
	@ScreenField(row = 15, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Job queue", sampleValue = "*JOBD", helpText = "Name, *JOBD")
    private String jobQueue;
	
	@ScreenField(row = 16, column = 39, endColumn= 48 ,labelColumn= 2 ,editable= true ,displayName = "Library", helpText = "Name, *LIBL, *CURLIB")
    private String jobQueueLibary;
	
	@ScreenField(row = 17, column = 37, endColumn= 41 ,labelColumn= 2 ,editable= true ,displayName = "Job priority on JOBQ", sampleValue = "*JOBD")
    private String jobPriorityonJobq;
	
	@ScreenField(row = 18, column = 37, endColumn= 41 ,labelColumn= 2 ,editable= true ,displayName = "Output priority on OUTQ", sampleValue = "*JOBD")
    private String outputPriorityonOutq;
	
	@ScreenField(row = 19, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Print device", sampleValue = "*CURRENT", helpText = "Name, *CURRENT, *USRPRF...")
    private String printDevice;


    


 
}
