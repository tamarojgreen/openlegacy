package apps.inventory.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.SendKeyClasses.F3;

import java.util.List;

@ScreenEntity(displayName = "Items List")
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 30, value = "Work with Item Master"),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "1") }, exitAction = F3.class)
public class ItemsList {

	@FieldMapping(row = 21, column = 19)
	private String positionTo;

	private List<ItemsListRow> itemListRows;

	@ScreenTable(startRow = 8, endRow = 19)
	public static class ItemsListRow {

		@ScreenColumn(startColumn = 65, endColumn = 68, key = true)
		private String itemNumber;

		@ScreenColumn(startColumn = 11, endColumn = 22)
		private String alphaSearch;

		@ScreenColumn(startColumn = 24, endColumn = 60)
		private String itemDescription;

	}
}
