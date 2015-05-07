package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity(displayName = "Change Profile")
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 20, value = "          Change Profile (CHGPRF)          "),
		@Identifier(row = 5, column = 2, value = "Assistance level . . . . . . . ."),
		@Identifier(row = 6, column = 2, value = "Current library  . . . . . . . .") })
@ScreenActions(actions = { @Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F10.class, displayName = "Additional parameters", alias = "additionalParameters"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = UserTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "9") })
public class ChangeProfilechgprf implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 5, column = 37, endColumn = 45, labelColumn = 2, editable = true, displayName = "Assistance level", sampleValue = "*SYSVAL")
	private String assistanceLevel;

	@ScreenField(row = 6, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "Current library", sampleValue = "RMR2L1")
	private String currentLibrary;

	@ScreenField(row = 7, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "Initial program to call", sampleValue = "SIGNONCLP")
	private String initialProgramToCall;

	@ScreenField(row = 8, column = 39, endColumn = 48, labelColumn = 2, editable = true, displayName = "Library", sampleValue = "QGPL")
	private String library1;

	@ScreenField(row = 9, column = 37, endColumn = 46, labelColumn = 2, editable = true, displayName = "Initial menu", sampleValue = "MAIN")
	private String initialMenu;

	@ScreenField(row = 10, column = 39, endColumn = 48, labelColumn = 2, editable = true, displayName = "Library", sampleValue = "*LIBL")
	private String library;

	@ScreenField(row = 11, column = 37, endColumn = 80, labelColumn = 2, editable = true, displayName = "Text description", sampleValue = "'ROI MOR'")
	private String textdescription;

}
