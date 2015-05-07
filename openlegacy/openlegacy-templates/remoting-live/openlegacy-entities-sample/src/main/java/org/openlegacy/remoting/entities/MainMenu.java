package org.openlegacy.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity(screenType = MenuEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 2, value = "MAIN      "),
		@Identifier(row = 19, column = 2, value = "Selection or command"),
		@Identifier(row = 1, column = 33, value = "i5/OS Main Menu") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F1.class, additionalKey = AdditionalKey.SHIFT, displayName = "Information Assistant", alias = "informationAssistant"),
		@Action(action = TerminalActions.F11.class, additionalKey = AdditionalKey.SHIFT, displayName = "Set initial menu", alias = "setInitialMenu") })
public class MainMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	@ScreenField(row = 20, column = 7, endColumn = 80, editable = true, displayName = "")
	private String menuSelection;

}
