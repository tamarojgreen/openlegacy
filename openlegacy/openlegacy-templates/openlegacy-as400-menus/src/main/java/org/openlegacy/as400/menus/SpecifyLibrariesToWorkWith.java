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

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 1, column = 2, value = "                       Specify Libraries to Work With                          "),
		@Identifier(row = 5, column = 4, value = "Library  . . . . . . . . . . ."),
		@Identifier(row = 7, column = 4, value = "ASP number . . . . . . . . . .") })
@ScreenActions(actions = { @Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F5.class, displayName = "Refresh", alias = "refresh"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = ProgrammingDevelopmentManagerpdm.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class SpecifyLibrariesToWorkWith {

	@ScreenField(row = 5, column = 37, endColumn = 46, labelColumn = 4, editable = true, displayName = "Library", sampleValue = "*LIBL")
	private String library;

	@ScreenField(row = 7, column = 37, endColumn = 42, labelColumn = 4, editable = true, displayName = "ASP number", sampleValue = "*ALL")
	private String aspNumber;

	@ScreenField(row = 8, column = 37, endColumn = 46, labelColumn = 4, editable = true, displayName = "ASP device", sampleValue = "*")
	private String aspDevice;

}
