package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.menu.Menu.MenuSelectionField;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity(screenType = MenuEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 35, value = "Office Tasks"),
		@Identifier(row = 19, column = 2, value = "Selection or command") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F9.class, displayName = "Retrieve", alias = "retrieve"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F1.class, additionalKey = AdditionalKey.SHIFT, displayName = "Information Assistant", alias = "informationAssistant"),
		@Action(action = TerminalActions.F4.class, additionalKey = AdditionalKey.SHIFT, displayName = "System Main menu", alias = "systemMainMenu") })
@ScreenNavigation(accessedFrom = IbmIMainMenu.class, assignedFields = { @AssignedField(field = "menuSelection", value = "2") })
public class OfficeTasks implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 2, column = 72, endColumn = 79, labelColumn = 62, displayName = "System", sampleValue = "S1051C6E")
	private String system;

	@ScreenField(row = 20, column = 7, endColumn = 80, editable = true, fieldType = MenuSelectionField.class, displayName = "Menu Selection")
	private String menuSelection;

}
