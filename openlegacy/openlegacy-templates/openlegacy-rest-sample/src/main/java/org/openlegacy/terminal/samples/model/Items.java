package org.openlegacy.terminal.samples.model;

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
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.modules.table.RecordSelectionEntity;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity(screenType = RecordSelectionEntity.class)
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.                                    ") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F6.class, displayName = "Create", alias = "create"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") })
@ScreenNavigation(accessedFrom = InventoryMenu.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class Items {

	@ScreenField(row = 21, column = 19, endColumn = 28, labelColumn = 2, editable = true, displayName = "Position to")
	private String positionTo;

	private List<ItemsRecord> itemsRecords;

	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = {
			@TableAction(actionValue = "2", displayName = "Revise", targetEntity = ItemDetails.class, defaultAction = true),
			@TableAction(actionValue = "4", displayName = "Delete", targetEntity = ItemDetails.class),
			@TableAction(actionValue = "5", displayName = "Display", targetEntity = ItemDetails.class) })
	public static class ItemsRecord {

		@ScreenColumn(startColumn = 4, endColumn = 4, editable = true, selectionField = true, displayName = "Action")
		private Integer action_;

		@ScreenColumn(startColumn = 11, endColumn = 20, displayName = "Alpha search", sampleValue = "APPLE")
		private String alphaSearch;

		@ScreenColumn(startColumn = 24, endColumn = 63, mainDisplayField = true, displayName = "Item Description", sampleValue = "Water Ball - Balls")
		private String itemDescription;

		@ScreenColumn(startColumn = 65, endColumn = 79, key = true, displayName = "Item Number", sampleValue = "3002")
		private Integer itemNumber;

	}

}
