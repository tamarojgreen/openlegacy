package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 28, value = "Display Job Run Attributes              "), 
				@Identifier(row = 5, column = 2, value = "Run priority  . . . . . . . . . . . . . . . . . . :"), 
				@Identifier(row = 6, column = 2, value = "Time slice in milliseconds  . . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F9.class, displayName = "Change job", alias = "changeJob"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Job menu", alias = "jobMenu") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "3") })
public class DisplayJobRunAttributes {
    
	
	@ScreenField(row = 3, column = 9, endColumn= 18 ,labelColumn= 2 ,displayName = "Job", sampleValue = "QPADEV007R")
    private String job;
	
	@ScreenField(row = 3, column = 32, endColumn= 41 ,labelColumn= 24 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(key = true, row = 3, column = 57, endColumn= 62 ,labelColumn= 47 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(row = 5, column = 56, endColumn= 65)
    private String runPriority;
	
	@ScreenField(row = 6, column = 56, endColumn= 65)
    private String timeSliceInMilliseconds;
	
	@ScreenField(row = 7, column = 56, endColumn= 59)
    private String eligiableForPurge;
	
	@ScreenField(row = 8, column = 56, endColumn= 65)
    private String defaultWaitTimeInSeconds;
	
	@ScreenField(row = 9, column = 56, endColumn= 65)
    private String maximumCpuTimeInMilliseconds;
	
	@ScreenField(row = 10, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "CPU time used", sampleValue = "32")
    private String cpuTimeUsed;
	
	@ScreenField(row = 11, column = 56, endColumn= 65)
    private String maximumTemporaryStorageInMegabytes;
	
	@ScreenField(row = 12, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Temporary storage used", sampleValue = "3")
    private String temporaryStorageUsed;
	
	@ScreenField(row = 13, column = 56, endColumn= 65)
    private String maximumThreads;
	
	@ScreenField(row = 14, column = 58, endColumn= 67 ,labelColumn= 4 ,displayName = "Threads", sampleValue = "1")
    private Integer threads;
	
	@ScreenField(row = 16, column = 56, endColumn= 65 ,labelColumn= 4 ,displayName = "Group", sampleValue = "*NOGROUP")
    private String group;
	
	@ScreenField(row = 17, column = 56, endColumn= 65 ,labelColumn= 4 ,displayName = "Level", sampleValue = "*NORMAL")
    private String level;
	
	@ScreenField(row = 18, column = 56, endColumn= 65)
    private String resourcesAffinityGroup;


    


 
}
