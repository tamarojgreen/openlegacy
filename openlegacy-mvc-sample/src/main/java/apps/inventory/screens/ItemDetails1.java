package apps.inventory.screens;

import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number . ."),
		@Identifier(row = 7, column = 2, value = "Item Description") })
public class ItemDetails1 {

	@ScreenField(row = 6, column = 33)
	private String itemNumber;

	@ScreenField(row = 7, column = 33, editable = true)
	private String itemDescription;

	private ItemDetails2 itemDetails2;

}
