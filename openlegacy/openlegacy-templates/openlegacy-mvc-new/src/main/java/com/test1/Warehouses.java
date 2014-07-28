package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.List;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.modules.table.RecordSelectionEntity;  

@ScreenEntity(screenType=RecordSelectionEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "Warehouses  "), 
				@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.                                    ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F6.class, displayName = "Create", alias = "create"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel") 
				})
@ScreenNavigation(accessedFrom = InventoryMenu.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "2")
					 }						
					)
public class Warehouses {
    


    
    private List<WarehousesRecord> warehousesRecords;


	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = { 
			@TableAction(actionValue = "2", displayName = "Revise", targetEntity=WarehouseDetails.class, defaultAction = true), 
			@TableAction(actionValue = "4", displayName = "Delete"), 
			@TableAction(actionValue = "5", displayName = "Display"), 
			@TableAction(actionValue = "6", displayName = "Items"), 
			@TableAction(actionValue = "7", displayName = "Locations") 
			})
	public static class WarehousesRecord {
		@ScreenColumn(startColumn = 4, endColumn = 4, editable=true, selectionField=true ,displayName = "Action")
		private Integer action_;
		@ScreenColumn(startColumn = 10, endColumn = 12, key=true ,displayName = "Code", sampleValue="001")
		private Integer code;
		@ScreenColumn(startColumn = 16, endColumn = 55, mainDisplayField=true ,displayName = "Description", sampleValue="Magics warehouse")
		private String description;
		
		
	}    
 
}
