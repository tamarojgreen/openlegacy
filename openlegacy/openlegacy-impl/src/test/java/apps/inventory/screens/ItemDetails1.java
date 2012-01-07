package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number . ."),
		@Identifier(row = 7, column = 2, value = "Item Description") })
@ScreenNavigation(accessedFrom = ItemsList.class, exitAction = TerminalActions.F12.class)
public class ItemDetails1 {

	@ScreenField(row = 6, column = 33)
	private String itemNumber;

	@ScreenField(row = 7, column = 33, editable = true)
	private String itemDescription;

	@ScreenField(row = 14, column = 37, endColumn = 76, displayName = "Item Class", sampleValue = "Vegetables")
	private String itemClass1;

	private ItemDetails2 itemDetails2;

}
