package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F3;

import java.util.List;

@ScreenEntity(displayName = "Items List")
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 30, value = "Work with Item Master"),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.") })
@ScreenActions(actions = { @Action(action = TerminalActions.F6.class, additionalKey = AdditionalKey.SHIFT, displayName = "Number Seq") })
@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "1") }, exitAction = F3.class)
public class ItemsList {

	@ScreenField(row = 21, column = 19)
	private String positionTo;

	private List<ItemsListRow> itemListRows;

	@ScreenField(row = 24, column = 2)
	private String errorMessage;

	@ScreenTable(startRow = 8, endRow = 19, supportTerminalData = true)
	@ScreenTableDrilldown
	public static class ItemsListRow {

		@ScreenColumn(startColumn = 4, endColumn = 5, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 65, endColumn = 68, key = true)
		private Integer itemNumber;

		@ScreenColumn(startColumn = 11, endColumn = 22)
		private String alphaSearch;

		@ScreenColumn(startColumn = 24, endColumn = 60)
		private String itemDescription;

	}

}
