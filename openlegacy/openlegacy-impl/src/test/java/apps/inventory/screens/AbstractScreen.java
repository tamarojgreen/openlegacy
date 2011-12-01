package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 48, value = "System") })
@ScreenActions(actions = { @Action(action = TerminalActions.ENTER.class, displayName = "Enter") })
public class AbstractScreen {

	@ScreenField(row = 24, column = 2, fieldType = Login.ErrorField.class)
	protected String error;

}
