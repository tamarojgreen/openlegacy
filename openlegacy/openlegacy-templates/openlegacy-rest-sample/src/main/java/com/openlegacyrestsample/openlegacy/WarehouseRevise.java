package com.openlegacyrestsample.openlegacy;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.F12;

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "  Work with Warehouse Details "),
		@Identifier(row = 6, column = 2, value = "Warehouse Number . . . . . . ."),
		@Identifier(row = 7, column = 34, value = "true", attribute = FieldAttributeType.Editable),
		@Identifier(row = 6, column = 34, value = "false", attribute = FieldAttributeType.Editable) })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = Warehouses.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "act", value = "2") })
public class WarehouseRevise {
    
	
	@ScreenField(row = 6, column = 34, endColumn = 36, labelColumn = 2, displayName = "Warehouse Number", sampleValue = "1")
    private String warehouseNumber;
	
	@ScreenField(row = 7, column = 34, endColumn= 73 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Description", sampleValue = "Discount warehouse")
    private String warehouseDescription;
	
	@ScreenField(row = 8, column = 34, endColumn= 35 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Type", sampleValue = "01", helpText = "Main")
    private String warehouseType;
	
	@ScreenField(row = 8, column = 37, endColumn = 76, displayName = "Warehouse Type Name")
    private String warehouseTypeName;
	
	@ScreenField(row = 10, column = 34, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "Replenishment Cycle flag")
    private String replenishmentCycleFlag;
	
	@ScreenField(row = 12, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Amended date")
    private String amendedDate;
	
	@ScreenField(row = 14, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created Date")
    private String createdDate;
	
	@ScreenField(row = 15, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created by", sampleValue = "OPENLEGA1")
    private String createdBy;

	@ScreenField(endColumn = 43, row = 13, column = 34, labelColumn = 2, endRow = 13, displayName = "Amended By")
	private String amendedBy;

	public enum CostingType implements EnumGetValue {
		costingType1("1", "1"), costingType2("2", "2"), costingType3("3", "3");
		private String value;
		private String display;

		CostingType(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	@ScreenField(row = 9, column = 34, endColumn = 34, labelColumn = 2, editable = true, endRow = 9, sampleValue = "1", displayName = "Costing Type")
	private CostingType costingType;


    


 
}
