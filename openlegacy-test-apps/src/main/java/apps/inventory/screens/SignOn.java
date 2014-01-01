package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.globals.Globals.GlobalField;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity(supportTerminalData = true, screenType = Login.LoginEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") })
@ScreenActions(actions = { @Action(action = TerminalActions.ESC.class, displayName = "Escape") })
public class SignOn extends AbstractScreen {

	@ScreenField(row = 2, column = 70, endColumn = 77, displayName = "System", sampleValue = "S44R5550", labelColumn = 48, fieldType = GlobalField.class)
	private String system;

	@ScreenField(row = 6, column = 53, endColumn = 62, fieldType = Login.UserField.class, editable = true, labelColumn = 17)
	private String user;

	@ScreenField(row = 7, column = 53, endColumn = 62, fieldType = Login.PasswordField.class, editable = true, password = true)
	private String password;

	@ScreenField(row = 8, column = 53, editable = true)
	private String programProcedure;

	@ScreenField(row = 24, column = 45, endColumn = 62)
	private String message;

}
