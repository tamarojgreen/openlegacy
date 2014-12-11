package org.openlegacy.as400.menus;

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

@ScreenEntity(displayName = "Copy From PC Document")
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 20, value = "     Copy From PC Document (CPYFRMPCD)     "),
		@Identifier(row = 5, column = 2, value = "From folder  . . . . . . . . . ."),
		@Identifier(row = 7, column = 2, value = "To file  . . . . . . . . . . . .") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F1.class, additionalKey = AdditionalKey.SHIFT, displayName = "How to use this display", alias = "howToUseThisDisplay") })
@ScreenNavigation(accessedFrom = IbmIAccessTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class CopyFromPcDocumentcpyfrmpcd {

	@ScreenField(row = 5, column = 37, endColumn = 80, labelColumn = 2, editable = true, displayName = "From folder")
	private String fromFolder;

	@ScreenField(row = 7, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "To file")
	private String toFile;

	@ScreenField(row = 8, column = 39, endColumn = 48, labelColumn = 2, editable = true, displayName = "Library", sampleValue = "*LIBL")
	private String library1;

	@ScreenField(row = 9, column = 37, endColumn = 48, labelColumn = 2, editable = true, displayName = "From document")
	private String fromDocument;

	@ScreenField(row = 10, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "To member", sampleValue = "*FIRST")
	private String toMember;

	@ScreenField(row = 11, column = 37, endColumn = 44, labelColumn = 2, editable = true, displayName = "Replace or add records", sampleValue = "*REPLACE")
	private String replaceOrAddRecords;

	@ScreenField(row = 12, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "Translate table", sampleValue = "*DFT")
	private String translateTable;

	@ScreenField(row = 13, column = 39, endColumn = 48, labelColumn = 2, editable = true, displayName = "Library")
	private String library;

	@ScreenField(row = 14, column = 37, endColumn = 43, labelColumn = 2, editable = true, displayName = "Format of PC data", sampleValue = "*TEXT")
	private String formatOfPcData;

	@ScreenField(row = 15, column = 37, endColumn = 41, labelColumn = 2, editable = true, displayName = "DBCS code page", sampleValue = "*DFT")
	private String dbcsCodePage;

	@ScreenField(row = 16, column = 37, endColumn = 40, labelColumn = 2, editable = true, displayName = "Insert DBCS SO/SI", sampleValue = "*YES")
	private String insertDbcsSosi;

}
