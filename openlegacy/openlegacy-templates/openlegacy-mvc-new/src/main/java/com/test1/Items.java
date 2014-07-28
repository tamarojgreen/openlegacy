package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.List;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.modules.table.RecordSelectionEntity;  

@ScreenEntity(screenType=RecordSelectionEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "Items     "), 
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
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class Items {
    


    
    private List<ItemsRecord> itemsRecords;


	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = { 
			@TableAction(actionValue = "2", displayName = "Edit", targetEntity=ItemDetails.class, defaultAction = true), 
			@TableAction(actionValue = "4", displayName = "Delete"), 
			@TableAction(actionValue = "5", displayName = "View") 
			})
	public static class ItemsRecord {
		@ScreenColumn(startColumn = 4, endColumn = 4, editable=true, selectionField=true ,displayName = "Action")
		private Integer action_;
		@ScreenColumn(startColumn = 11, endColumn = 14, key=true ,displayName = "Item Number", sampleValue="3002")
		private Integer itemNumber;
		@ScreenColumn(startColumn = 24, endColumn = 63, mainDisplayField=true ,displayName = "Item Description", sampleValue="Water Ball - Balls")
		private String itemDescription;
		@ScreenColumn(startColumn = 65, endColumn = 78 ,displayName = "Item Name", sampleValue="Water Ball")
		private String itemName;
		
		
	}    
 
}
