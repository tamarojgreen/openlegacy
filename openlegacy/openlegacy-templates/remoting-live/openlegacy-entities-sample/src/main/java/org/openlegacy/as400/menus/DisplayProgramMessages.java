package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity(screenType = MessagesEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 16, value = "            Display Program Messages            "),
		@Identifier(row = 19, column = 2, value = "Press Enter to continue.                   ") })
@ScreenActions(actions = { @Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
public class DisplayProgramMessages implements Serializable {

	private static final long serialVersionUID = 1L;
	@ScreenField(row = 3, column = 2, endColumn = 77, endRow = 4, fieldType = MessageField.class, displayName = "")
	private String message;

}
