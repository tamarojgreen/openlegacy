package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Change Printer Output"),
		@Identifier(row = 4, column = 2, value = "Printer output . . . :"),
		@Identifier(row = 4, column = 43, value = "Time . . . . . . . . :") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = WorkWithPrinterOutput.class, drilldownValue = "2")
public class ChangePrinterOutput {

	@ScreenField(row = 3, column = 27, endColumn = 36, labelColumn = 2, displayName = "User", sampleValue = "RMR20924")
	private String user;

	@ScreenField(row = 3, column = 68, endColumn = 75, labelColumn = 43, displayName = "Date", sampleValue = "10/09/13")
	private String date;

	@ScreenField(row = 4, column = 27, endColumn = 36, labelColumn = 2, displayName = "Printer output", sampleValue = "DGFLAT")
	private String printerOutput;

	@ScreenField(row = 4, column = 68, endColumn = 75, labelColumn = 43, displayName = "Time", sampleValue = "00:53:17")
	private String time;

	@ScreenField(key = true, row = 5, column = 27, endColumn = 32, labelColumn = 2, displayName = "Pages", sampleValue = "3")
	private Integer pages;

	@ScreenField(row = 13, column = 46, endColumn = 48, labelColumn = 6, editable = true, displayName = "Number of copies", sampleValue = "1")
	private Integer numberOfCopies;

	@ScreenField(row = 14, column = 46, endColumn = 50, labelColumn = 6, editable = true, displayName = "First page to print", sampleValue = "1", helpText = "Number")
	private Integer firstPageToPrint;

	@ScreenField(row = 15, column = 46, endColumn = 50, labelColumn = 6, editable = true, displayName = "Last page to print", sampleValue = "*LAST", helpText = "Number, *LAST")
	private String lastPageToPrint;

	@ScreenField(row = 17, column = 46, endColumn = 55, labelColumn = 4, editable = true, displayName = "Type of forms", sampleValue = "*STD", helpText = "Form type, *STD")
	private String typeOfForms;

	@ScreenField(row = 19, column = 46, endColumn = 46, labelColumn = 4, editable = true, displayName = "Print this output next", sampleValue = "N")
	private String printThisOutputNext;

	@ScreenField(row = 19, column = 48, endColumn = 57, labelColumn = 4, displayName = "Print this output next")
	private String printThisOutputNext1;

	@ScreenField(row = 21, column = 46, endColumn = 46, labelColumn = 4, editable = true, displayName = "Save printer output", sampleValue = "N")
	private String savePrinterOutput;

	@ScreenField(row = 21, column = 48, endColumn = 57, labelColumn = 4, displayName = "Save printer output")
	private String savePrinterOutput1;

	@ScreenFieldValues(sourceScreenEntity = SelectPrinter.class)
	@ScreenField(row = 10, column = 46, endColumn = 55, labelColumn = 4, editable = true, endRow = 10, displayName = "Printer to use", sampleValue = "RMR2P1", helpText = "Name, F4 for list")
	private String printerToUse;

}
