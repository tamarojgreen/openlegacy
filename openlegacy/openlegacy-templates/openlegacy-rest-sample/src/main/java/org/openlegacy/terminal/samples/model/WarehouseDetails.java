package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.Date;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "  Work with Warehouse Details "),
		@Identifier(row = 4, column = 2, value = "Type choices, press Enter."),
		@Identifier(row = 6, column = 2, value = "Warehouse Number . . . . . . .") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F2.class, additionalKey = AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") })
@ScreenNavigation(accessedFrom = Warehouses.class)
public class WarehouseDetails {

	@ScreenField(row = 6, column = 34, endColumn = 36, key = true, labelColumn = 2, editable = true, displayName = "Warehouse Number", sampleValue = "003")
	private Integer warehouseNumber;

	@ScreenField(row = 7, column = 34, endColumn = 73, labelColumn = 2, editable = true, displayName = "Warehouse Description", sampleValue = "Discount warehouse")
	private String warehouseDescription;

	@ScreenFieldValues(sourceScreenEntity = WarehouseTypes.class)
	@ScreenField(row = 8, column = 34, labelColumn = 2, editable = true, displayName = "Warehouse Type", sampleValue = "GL")
	private String warehouseType;

	@ScreenField(row = 8, column = 37, endColumn = 76, labelColumn = 2, displayName = "Warehouse Type", sampleValue = "General warehouse")
	private String warehouseTypeName;

	@ScreenField(row = 9, column = 34, endColumn = 34, labelColumn = 2, editable = true, displayName = "Costing Type", sampleValue = "1")
	private Integer costingType;

	@ScreenField(row = 10, column = 34, endColumn = 34, labelColumn = 2, editable = true, displayName = "Replenishment Cycle flag")
	private String replenishmentCycleFlag;

	@ScreenField(row = 11, column = 20, labelColumn = 2, endColumn = 74, editable = true, displayName = "Address")
	private String address;

	@ScreenField(row = 12, column = 20, labelColumn = 2, endColumn = 31, editable = true, displayName = "Phone")
	private String phone;

	@ScreenField(row = 13, column = 20, labelColumn = 2, endColumn = 44, editable = true, displayName = "Email")
	private String email;

	@ScreenDateField(dayColumn = 34, monthColumn = 37, yearColumn = 40)
	@ScreenField(row = 14, column = 34, endColumn = 42, labelColumn = 2, editable = true, displayName = "Amended date", sampleValue = "23")
	private Date amendedDate;

	@ScreenField(row = 15, column = 34, endColumn = 43, labelColumn = 2, displayName = "Amended By", sampleValue = "TESTUSER")
	private String amendedBy;

	@ScreenField(row = 16, column = 34, endColumn = 43, labelColumn = 2, displayName = "Created Date", sampleValue = "21/12/2002")
	private String createdDate;

	@ScreenField(row = 17, column = 34, endColumn = 43, labelColumn = 2, displayName = "Created by", sampleValue = "Terry")
	private String createdBy;

}
