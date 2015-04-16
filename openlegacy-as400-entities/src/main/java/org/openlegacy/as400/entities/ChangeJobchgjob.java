package org.openlegacy.as400.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 32, value = "Change Job (CHGJOB)            "), 
				@Identifier(row = 5, column = 2, value = "Job name . . . . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"), 
				@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = WorkWithJobMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "40") })
public class ChangeJobchgjob {
    
	
	@ScreenField(row = 5, column = 37, endColumn= 46 ,labelColumn= 2 ,displayName = "Job name", sampleValue = "QPADEV007R")
    private String jobName;
	
	@ScreenField(row = 6, column = 39, endColumn= 47 ,labelColumn= 2 ,displayName = "User", sampleValue = "OPENLEGA1")
    private String user;
	
	@ScreenField(key = true, row = 7, column = 39, endColumn= 44 ,labelColumn= 2 ,displayName = "Number", sampleValue = "033328")
    private String number;
	
	@ScreenField(row = 10, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Print device", sampleValue = "PRT01", helpText = "Name, *SAME, *USRPRF...")
    private String printDevice;
	
	@ScreenField(row = 11, column = 37, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Output queue", sampleValue = "OPENLEGA1", helpText = "Name, *SAME, *USRPRF, *DEV...")
    private String outputQueue;
	
	@ScreenField(row = 12, column = 39, endColumn= 48 ,labelColumn= 2 ,editable= true ,displayName = "Library", sampleValue = "QGPL", helpText = "Name, *LIBL, *CURLIB")
    private String library;
	
	@ScreenField(row = 13, column = 37, endColumn= 42 ,labelColumn= 2 ,editable= true ,displayName = "Run priority", sampleValue = "20", helpText = "1-99, *SAME")
    private Integer runPriority;

	public enum JobPriorityonJobq implements EnumGetValue {
		jobPriorityonJobq1("0", "0"), jobPriorityonJobq2("1", "1"), jobPriorityonJobq3(
				"2", "2"), jobPriorityonJobq4("3", "3"), jobPriorityonJobq5(
				"4", "4"), jobPriorityonJobq6("5", "5"), jobPriorityonJobq7(
				"6", "6"), jobPriorityonJobq8("7", "7"), jobPriorityonJobq9(
				"8", "8"), jobPriorityonJobq10("9", "9"), jobPriorityonJobq11(
				"*SAME", "*SAME");
		private String value;
		private String display;

		JobPriorityonJobq(String value, String display) {
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

	@ScreenField(row = 8, column = 37, endColumn = 41, labelColumn = 2, sampleValue = "*SAME", editable = true, endRow = 8, displayName = "Job priority on JOBQ")
	private JobPriorityonJobq jobPriorityonJobq;

	public enum OutputPriorityonOutq implements EnumGetValue {
		outputPriorityonOutq1("1", "1"), outputPriorityonOutq2("2", "2"), outputPriorityonOutq3(
				"3", "3"), outputPriorityonOutq4("4", "4"), outputPriorityonOutq5(
				"5", "5"), outputPriorityonOutq6("6", "6"), outputPriorityonOutq7(
				"7", "7"), outputPriorityonOutq8("8", "8"), outputPriorityonOutq9(
				"9", "9"), outputPriorityonOutq10("*SAME", "*SAME");
		private String value;
		private String display;

		OutputPriorityonOutq(String value, String display) {
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

	@ScreenField(row = 9, column = 37, endColumn = 41, labelColumn = 2, editable = true, endRow = 9, sampleValue = "5", displayName = "Output priority on OUTQ")
	private OutputPriorityonOutq outputPriorityonOutq;


    


 
}
