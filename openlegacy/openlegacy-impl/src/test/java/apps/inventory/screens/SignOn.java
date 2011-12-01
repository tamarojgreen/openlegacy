package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity(supportTerminalData = true, screenType = Login.LoginScreen.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") })
@ScreenActions(actions = { @Action(action = TerminalActions.ENTER.class, displayName = "Enter") })
public class SignOn {

	@ScreenField(row = 6, column = 53, fieldType = Login.UserField.class, editable = true)
	private String user;

	@ScreenField(row = 7, column = 53, fieldType = Login.PasswordField.class, editable = true)
	private String password;

	@ScreenField(row = 8, column = 53, editable = true)
	private String programProcedure;

	@ScreenField(row = 24, column = 2, fieldType = Login.ErrorField.class)
	private String error;

}
