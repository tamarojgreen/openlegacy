package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity(window = true)
@ScreenIdentifiers(identifiers = { @Identifier(row = 4, column = 19, value = "    List of Warehouse Types   "),
		@Identifier(row = 6, column = 6, value = "Type options, press Enter."),
		@Identifier(row = 16, column = 6, value = "F12=Cancel") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, additionalKey = AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") })
@ScreenNavigation(accessedFrom = WarehouseDetails.class, assignedFields = { @AssignedField(field = "warehouseType") })
public class WarehouseTypes {

	private List<WarehouseTypesRecord> warehouseTypesRecords;

	@ScreenTable(startRow = 10, endRow = 14)
	public static class WarehouseTypesRecord {

		@ScreenColumn(startColumn = 8, endColumn = 8, editable = true, selectionField = true, displayName = "Action")
		private Integer action_;

		@ScreenColumn(startColumn = 14, endColumn = 15, key = true, displayName = "Type", sampleValue = "DR")
		private String type;

		@ScreenColumn(startColumn = 20, endColumn = 59, mainDisplayField = true, displayName = "Description", sampleValue = "Dairy warehouse")
		private String description;

	}

}
