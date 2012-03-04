package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenBooleanField;
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

	@ScreenField(row = 7, column = 33, endColumn = 40, editable = true)
	private String itemDescription;

	@ScreenBooleanField(trueValue = "Y", falseValue = "N")
	@ScreenField(row = 20, column = 33, editable = true)
	private Boolean palletLabelRequired;

	private ItemDetails2 itemDetails2;

}
