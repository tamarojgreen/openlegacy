package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import java.util.Date;  

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "  Warehouse Details "), 
				@Identifier(row = 4, column = 2, value = "Type choices, press Enter."), 
				@Identifier(row = 6, column = 2, value = "Warehouse Number . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = Warehouses.class 
					, drilldownValue="2", requiresParameters=true)
public class WarehouseDetails {
    
	
	@ScreenField(key = true, row = 6, column = 34, endColumn= 36 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Number", sampleValue = "003")
    private Integer warehouseNumber;
	
	@ScreenField(row = 7, column = 34, endColumn= 73 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Description", sampleValue = "Discount warehouse")
    private String warehouseDescription;
	
	@ScreenFieldValues(sourceScreenEntity=WarehouseTypes.class,collectAll=false)
	@ScreenField(row = 8, column = 34, endColumn= 35 ,labelColumn= 2 ,editable= true ,displayName = "Warehouse Type", sampleValue = "GL", helpText = "General warehouse")
    private String warehouseType;
	
	@ScreenField(row = 9, column = 34, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "Costing Type", sampleValue = "1")
    private Integer costingType;
	
	@ScreenField(row = 11, column = 34, endColumn= 78 ,labelColumn= 2 ,editable= true ,displayName = "Address", sampleValue = "4309 S Morgan St, Chicago, IL, 60609, US")
    private String address;
	
	@ScreenField(row = 12, column = 34, endColumn= 45 ,labelColumn= 2 ,editable= true ,displayName = "Phone", sampleValue = "919.372.3412")
    private String phone;
	
	@ScreenField(row = 13, column = 34, endColumn= 58 ,labelColumn= 2 ,editable= true ,displayName = "Email", sampleValue = "chicago.wh@legacytoys.com")
    private String email;
	
	@ScreenDateField(dayColumn=34, monthColumn=39, yearColumn=44)
	@ScreenField(row = 14, column = 34, endColumn= 46 ,labelColumn= 2 ,editable= true ,displayName = "Amended date", sampleValue = "23")
    private Date amendedDate;
	
	@ScreenField(row = 15, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Amended By", sampleValue = "TESTUSER")
    private String amendedBy;
	
	@ScreenField(row = 16, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created Date", sampleValue = "21/12/2002")
    private String createdDate;
	
	@ScreenField(row = 17, column = 34, endColumn= 43 ,labelColumn= 2 ,displayName = "Created by", sampleValue = "Terry")
    private String createdBy;


    


 
}
