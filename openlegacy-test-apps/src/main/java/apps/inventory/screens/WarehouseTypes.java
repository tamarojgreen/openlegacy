package apps.inventory.screens;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.TerminalActions.F3;

import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 4, column = 23, value = "List of Warehouse Types") })
@ScreenNavigation(accessedFrom = WarehouseDetails.class, assignedFields = { @AssignedField(field = "warehouseType") }, exitAction = F3.class)
public class WarehouseTypes {

	private List<WarehouseTypeRow> warehouseTypeRows;

	@ScreenTable(startRow = 10, endRow = 13)
	public static class WarehouseTypeRow {

		@ScreenColumn(startColumn = 8, endColumn = 8, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 14, endColumn = 15, key = true)
		private String warehouseType;

		@ScreenColumn(startColumn = 20, endColumn = 59, mainDisplayField = true)
		private String warehouseTypeDescription;

	}
}
