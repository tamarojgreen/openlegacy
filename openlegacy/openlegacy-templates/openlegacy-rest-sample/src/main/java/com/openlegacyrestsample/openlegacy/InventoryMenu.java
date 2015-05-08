package com.openlegacyrestsample.openlegacy;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity(screenType = MenuEntity.class, name = "InventoryMenu", displayName = "Inventory Menu")
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 16, value = "               Inventory Management               ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Home", alias = "home"), 
				@Action(action = TerminalActions.F12.class, displayName = "Prev", alias = "prev"), 
				@Action(action = TerminalActions.F9.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Command", alias = "command") 
				})
@ScreenNavigation(accessedFrom = InventoryAndPurchasingMenu.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class InventoryMenu {
    
	
	@ScreenField(endColumn = 9, row = 21, column = 8, editable = true, endRow = 21, displayName = "Menu Selection")
	private String menuSelection;


    


 
}