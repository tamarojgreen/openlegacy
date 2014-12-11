package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 32, value = "Edit Library List"),
		@Identifier(row = 5, column = 2, value = "Sequence"), @Identifier(row = 5, column = 29, value = "Sequence") })
@ScreenActions(actions = { @Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = UserTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "7") })
public class EditLibraryList {

	@ScreenField(row = 2, column = 72, endColumn = 79, labelColumn = 62, displayName = "System", sampleValue = "S1051C6E")
	private String system;

	private List<EditLibraryListRecord> editLibraryListRecords;

	@ScreenTable(startRow = 7, endRow = 21)
	public static class EditLibraryListRecord {

		@ScreenColumn(startColumn = 4, endColumn = 7, editable = true, key = true, displayName = "Number", sampleValue = "0")
		private Integer number;

		@ScreenColumn(startColumn = 12, endColumn = 21, editable = true, mainDisplayField = true, displayName = "Library")
		private String library;

		@ScreenColumn(startColumn = 31, endColumn = 34, editable = true, displayName = "Number", sampleValue = "150")
		private Integer number1;

		@ScreenColumn(startColumn = 39, endColumn = 48, editable = true, displayName = "Library")
		private String library1;

		@ScreenColumn(startColumn = 58, endColumn = 61, editable = true, displayName = "Number", sampleValue = "300")
		private Integer number2;

		@ScreenColumn(startColumn = 66, endColumn = 75, editable = true, displayName = "Library")
		private String library2;

	}

}
