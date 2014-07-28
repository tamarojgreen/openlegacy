package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

@ScreenEntity(child=true)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "    Stock Details     "), 
				@Identifier(row = 4, column = 2, value = "Type choices, press Enter."), 
				@Identifier(row = 6, column = 2, value = "Item Number . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Return", alias = "return_"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = ItemDetails.class 
					, assignedFields = { 
					@AssignedField(field = "itemDescription")
					 }						
					)
public class StockDetails {
    
	
	@ScreenField(key = true, row = 6, column = 22, endColumn= 36 ,labelColumn= 2 ,displayName = "Item Number", sampleValue = "2000")
    private Integer itemNumber;
	
	@ScreenField(row = 7, column = 29, endColumn= 43 ,labelColumn= 2 ,editable= true ,displayName = "N/L Stock Account")
    private String nlStockAccount;
	
	@ScreenField(row = 8, column = 29, endColumn= 29 ,labelColumn= 2 ,editable= true ,displayName = "Item Type  business area")
    private String itemTypeBusinessArea;
	
	@ScreenField(row = 9, column = 29, endColumn= 29 ,labelColumn= 2 ,editable= true ,displayName = "Stock Analysis Code")
    private String stockAnalysisCode;
	
	@ScreenField(row = 10, column = 29, endColumn= 41 ,labelColumn= 2 ,editable= true ,displayName = "Standard unit cost")
    private Integer standardUnitCost;
	
	@ScreenField(row = 19, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Created date", sampleValue = "17/01/2010")
    private String createdDate;
	
	@ScreenField(row = 20, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Created By", sampleValue = "TESTUSER")
    private String createdBy;
	
	@ScreenField(row = 21, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Amended date", sampleValue = "0/00/0000")
    private String amendedDate;
	
	@ScreenField(row = 22, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Amended By")
    private String amendedBy;


    


 
}
