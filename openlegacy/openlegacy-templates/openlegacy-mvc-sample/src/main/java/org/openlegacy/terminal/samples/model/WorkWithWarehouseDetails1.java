package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.Date;  

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "  Work with Warehouse Details "), 
				@Identifier(row = 4, column = 2, value = "Type choices, press Enter."), 
				@Identifier(row = 6, column = 2, value = "Warehouse Number . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = WorkWithWarehouseDetails.class)
public class WorkWithWarehouseDetails1 {
    
	
	@ScreenField(row = 6, column = 34, endColumn= 36 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Number", sampleValue = "003")
    private Integer warehouseNumber;
	
	@ScreenField(row = 7, column = 34, endColumn= 73 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Description", sampleValue = "Discount warehouse")
    private String warehouseDescription;
	
	@ScreenFieldValues(sourceScreenEntity = ListOfWarehouseTypes.class)
	@ScreenField(row = 8, column = 34, endColumn= 35 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Type", sampleValue = "GL")
    private String warehouseType;
	
	@ScreenField(row = 8, column = 37, endColumn= 76 ,labelColumn= 2 ,displayName = "Warehouse Type", sampleValue = "General warehouse")
    private String warehouseType1;
	
	@ScreenField(row = 9, column = 34, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "Costing Type", sampleValue = "1")
    private Integer costingType;
	
	@ScreenField(row = 10, column = 34, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "Replenishment Cycle flag")
    private String replenishmentCycleFlag;
	
	@ScreenDateField(dayColumn=34, monthColumn=37, yearColumn=40)
	@ScreenField(row = 12, column = 34, endColumn= 42 ,labelColumn= 2 ,editable= true ,displayName = "Amended date", sampleValue = "23")
    private Date amendedDate;
	
	@ScreenField(row = 13, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Amended By", sampleValue = "TESTUSER")
    private String amendedBy;
	
	@ScreenField(row = 14, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created Date", sampleValue = "21/12/2002")
    private String createdDate;
	
	@ScreenField(row = 15, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created by", sampleValue = "Terry")
    private String createdBy;

    


 
}
