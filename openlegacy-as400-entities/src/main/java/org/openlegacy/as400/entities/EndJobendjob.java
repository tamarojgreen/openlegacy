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
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 33, value = "End Job (ENDJOB)              "), 
				@Identifier(row = 5, column = 2, value = "Job name . . . . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "41") })
public class EndJobendjob {
    
	
	@ScreenField(row = 5, column = 37, endColumn= 46 ,labelColumn= 2 ,displayName = "Job name", sampleValue = "QPADEV007R")
    private String jobName;
	
	@ScreenField(row = 6, column = 39, endColumn= 47 ,labelColumn= 2 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(key = true, row = 7, column = 39, endColumn= 44 ,labelColumn= 2 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(row = 9, column = 37, endColumn= 47 ,labelColumn= 2 ,editable= true ,displayName = "Controlled end delay time", sampleValue = "30", helpText = "Seconds")
    private Integer controlledEndDelayTime;
	
	@ScreenField(row = 11, column = 37, endColumn= 47 ,labelColumn= 2 ,editable= true ,displayName = "Maximum log entries", sampleValue = "*SAME", helpText = "Number, *SAME, *NOMAX")
    private String maximumLogEntries;
	
	public enum HowToEnd implements EnumGetValue {
		howToEnd1("*CNTRLD", "*CNTRLD"), howToEnd2("*TMMED", "*TMMED");
		private String value;
		private String display;

		HowToEnd(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	@ScreenField(row = 8, column = 37, endColumn = 43, labelColumn = 2, sampleValue = "*CNTRLD", editable = true, endRow = 8, displayName = "How to end", helpText = "*CNTRLD, *IMMED")
	private HowToEnd howToEnd;

	@ScreenBooleanField(trueValue = "*YES", falseValue = "*NO")
	@ScreenField(endColumn = 40, row = 10, column = 37, labelColumn = 2, sampleValue = "*NO", editable = true, endRow = 10, displayName = "Delete spooled files")
	private Boolean deleteSpooledFiles;

	public enum AdditionalInteractiveJobs implements EnumGetValue {
		additionalInteractiveJobs1("*NONE", "*NONE"), additionalInteractiveJobs2(
				"*GRPJOB", "*GRPJOB"), additionalInteractiveJobs3("*ALL",
				"*ALL");
		private String value;
		private String display;

		AdditionalInteractiveJobs(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	@ScreenField(row = 12, column = 37, endColumn = 43, labelColumn = 2, sampleValue = "*NONE", editable = true, endRow = 12, helpText = "*NONE, *GRPJOB, *ALL", displayName = "Additional interactive jobs")
	private AdditionalInteractiveJobs additionalInteractiveJobs;


    


 
}
