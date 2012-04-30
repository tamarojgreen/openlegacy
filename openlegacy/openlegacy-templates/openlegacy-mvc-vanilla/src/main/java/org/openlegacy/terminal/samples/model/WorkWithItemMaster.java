package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.List;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.modules.table.RecordSelectionEntity;  

@ScreenEntity(screenType=RecordSelectionEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "    Work with Item Master     "), 
				@Identifier(row = 4, column = 2, value = "Type one or more action codes. Then Enter.                                    ") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"), 
				@Action(action = TerminalActions.F6.class, displayName = "Create", alias = "create"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F6.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Number Seq", alias = "numberSeq") 
				})
@ScreenNavigation(accessedFrom = InventoryManagement.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class WorkWithItemMaster {
    
	
	@ScreenField(row = 21, column = 19, endColumn= 28 ,labelColumn= 2 ,editable= true ,displayName = "Position to")
    private String positionTo;

    
    private List<WorkWithItemMasterRecord> workWithItemMasterRecords;


	@ScreenTable(startRow = 8, endRow = 19)
	@ScreenTableActions(actions = { 
			@TableAction(actionValue = "2", displayName = "Revise"), 
			@TableAction(actionValue = "4", displayName = "Delete"), 
			@TableAction(actionValue = "5", displayName = "Display") 
			})
	public static class WorkWithItemMasterRecord {
		@ScreenColumn(startColumn = 4, endColumn = 4, editable=true, selectionField=true ,displayName = "Action")
		private Integer action_;
		
		@ScreenColumn(startColumn = 11, endColumn = 20, key=true ,displayName = "Alpha search", sampleValue="APPLE")
		private String alphaSearch;
		
		@ScreenColumn(startColumn = 24, endColumn = 63, mainDisplayField=true ,displayName = "Item Description", sampleValue="Red apple - FRT")
		private String itemDescription;
		
		@ScreenColumn(startColumn = 65, endColumn = 79 ,displayName = "Item Number", sampleValue="1007")
		private Integer itemNumber;
		
	}    
 
}
