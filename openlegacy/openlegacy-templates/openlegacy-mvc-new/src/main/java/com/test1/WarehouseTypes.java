package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.List;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.modules.table.LookupEntity;  

@ScreenEntity(window=true, rows= 14, columns= 61,screenType=LookupEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 4, column = 19, value = "    Warehouse Types   "), 
				@Identifier(row = 6, column = 6, value = "Type options, press Enter."), 
				@Identifier(row = 16, column = 6, value = "F12=Cancel") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = WarehouseDetails.class 
					, terminalAction=TerminalActions.F4.class , assignedFields = { 
					@AssignedField(field = "warehouseType")
					 }						
					)
public class WarehouseTypes {
    


    
    private List<WarehouseTypesRecord> warehouseTypesRecords;


	@ScreenTable(startRow = 10, endRow = 13)
	public static class WarehouseTypesRecord {
		@ScreenColumn(startColumn = 8, endColumn = 8, editable=true, selectionField=true ,displayName = "Action")
		private Integer action_;
		@ScreenColumn(startColumn = 14, endColumn = 15, key=true ,displayName = "Type", sampleValue="ST")
		private String type;
		@ScreenColumn(startColumn = 20, endColumn = 60, mainDisplayField=true ,displayName = "Description", sampleValue="Sports Toys warehouse")
		private String description;
		
		
	}    
 
}
