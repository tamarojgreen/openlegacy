package org.openlegacy.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

import java.io.Serializable;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "  Work with Warehouse Details "),
		@Identifier(row = 6, column = 2, value = "Warehouse Number . . . . . . ."),
		@Identifier(row = 7, column = 34, value = "false", attribute = FieldAttributeType.Editable) })
@ScreenActions(actions = { @Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = Warehouses.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "act", value = "5") })
public class WarehouseDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 7, column = 34, endColumn = 73, displayName = "Warehouse Description")
	private String warehouseDescription;

	@ScreenField(row = 8, column = 37, endColumn = 76, displayName = "Warehouse Type Name")
	private String warehouseTypeName;

	@ScreenField(row = 12, column = 34, endColumn = 43, labelColumn = 2, displayName = "Amended date")
	private String amendedDate;

	@ScreenField(row = 13, column = 34, endColumn = 43, labelColumn = 2, displayName = "Amended By")
	private String amendedBy;

	@ScreenField(row = 14, column = 34, endColumn = 43, labelColumn = 2, displayName = "Created Date")
	private String createdDate;

	@ScreenField(row = 15, column = 34, endColumn = 43, labelColumn = 2, displayName = "Created by", sampleValue = "OPENLEGA1")
	private String createdBy;

	@ScreenField(endColumn = 35, row = 8, column = 34, labelColumn = 2, sampleValue = "01", endRow = 8, displayName = "Warehouse Type")
	private String warehouseType;

	@ScreenField(endColumn = 36, row = 6, column = 34, labelColumn = 2, sampleValue = "1", endRow = 6, displayName = "Warehouse Number")
	private String warehouseNumber;

	@ScreenField(endColumn = 34, row = 9, column = 34, labelColumn = 2, sampleValue = "1", endRow = 9, displayName = "Costing Type")
	private String costingType;

}
