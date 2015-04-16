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
				@Identifier(row = 1, column = 32, value = "Change Password          "), 
				@Identifier(row = 5, column = 2, value = "Password last changed . . . . . . . . . . :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = UserTasksMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "8") })
public class ChangePassword {
    
	
	@ScreenField(row = 3, column = 47, endColumn= 56 ,labelColumn= 2 ,displayName = "User profile", sampleValue = "OPENLEGA1")
    private String userProfile;
	
	@ScreenField(row = 5, column = 47, endColumn= 54 ,labelColumn= 2 ,displayName = "Password last changed", sampleValue = "17/03/15")
    private String passwordLastChanged;
	
	@ScreenField(row = 9, column = 47, endColumn= 56 ,labelColumn= 4 ,editable= true ,displayName = "Current password")
    private String currentPassword;
	
	@ScreenField(row = 11, column = 47, endColumn= 56 ,labelColumn= 4 ,editable= true ,displayName = "New password")
    private String newPassword;
	
	@ScreenField(row = 13, column = 47, endColumn= 56 ,labelColumn= 4 ,editable= true ,displayName = "New password to verify")
    private String newPasswordtoVerify;


    


 
}
