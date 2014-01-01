package apps.inventory.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.terminal.actions.TerminalActions.F3;

import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 38, value = "Warehouse Details"),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "2") }, exitAction = F3.class)
public class WarehousesList {

	private List<WarehousesListRow> warehousesListRows;

	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableDrilldown
	public static class WarehousesListRow {

		@ScreenColumn(startColumn = 4, endColumn = 4, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 10, endColumn = 12, key = true)
		private String warehouseNumber;

		@ScreenColumn(startColumn = 16, endColumn = 55)
		private String warehouseDescription;

	}

}
