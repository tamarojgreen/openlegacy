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

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 22, value = "          Change Password          "),
		@Identifier(row = 5, column = 2, value = "Password last changed . . . . . . . . . . :"),
		@Identifier(row = 7, column = 2, value = "Type choices, press Enter.                                     ") })
@ScreenActions(actions = { @Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = UserTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "8") })
public class ChangePassword implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 3, column = 47, endColumn = 56, labelColumn = 2, displayName = "User profile", sampleValue = "RMR20924")
	private String userProfile;

	@ScreenField(row = 5, column = 47, endColumn = 54, labelColumn = 2, displayName = "Password last changed", sampleValue = "09/24/12")
	private String passwordLastChanged;

	@ScreenField(row = 9, column = 47, endColumn = 56, labelColumn = 4, editable = true, displayName = "Current password")
	private String currentPassword;

	@ScreenField(row = 11, column = 47, endColumn = 56, labelColumn = 4, editable = true, displayName = "New password")
	private String newPassword;

	@ScreenField(row = 13, column = 47, endColumn = 56, labelColumn = 4, editable = true, displayName = "New password to verify")
	private String newPasswordtoVerify;

}
