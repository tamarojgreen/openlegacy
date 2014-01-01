package org.openlegacy.terminal.samples.model;

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

@ScreenEntity(screenType = MenuEntity.class)
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 1, column = 16, value = "               Inventory Management               "),
		@Identifier(row = 20, column = 2, value = "Selection:") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Home", alias = "home"),
		@Action(action = TerminalActions.F12.class, displayName = "Prev", alias = "prev"),
		@Action(action = TerminalActions.F9.class, additionalKey = AdditionalKey.SHIFT, displayName = "Command", alias = "command") })
@ScreenNavigation(accessedFrom = MainMenu.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class InventoryMenu {

	@ScreenField(row = 21, column = 8, endColumn = 9, editable = true, fieldType = MenuSelectionField.class, displayName = "Menu Selection", sampleValue = "1")
	private Integer menuSelection;

}
