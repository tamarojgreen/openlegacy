package com.openlegacy.ws.openlegacy;

import org.openlegacy.annotations.screen.Action;
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

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 7, column = 2, value = "N/L Cost of Sales Account"),
		@Identifier(row = 8, column = 29, value = "false", attribute = FieldAttributeType.Editable) })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F12.class, displayName = "Return", alias = "return_"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = ItemDetails.class, exitAction = F12.class)
public class ItemDetailsStock {
    
	
	@ScreenField(row = 6, column = 23, endColumn= 62 ,displayName = "Domino Cubes  Board")
    private String itemDescription;
	
	@ScreenField(row = 7, column = 29, endColumn= 43 ,displayName = "N/L Cost of Sales Account")
    private String nlCostOfSalesAccount;
	
	@ScreenField(row = 8, column = 29, endColumn= 43 ,labelColumn= 2 ,displayName = "N/L Sales Account")
    private String nlSalesAccount;
	
	@ScreenField(row = 9, column = 29, endColumn= 43 ,labelColumn= 2 ,displayName = "N/L Stock Account")
    private String nlStockAccount;
	
	@ScreenField(row = 10, column = 29, endColumn= 33 ,displayName = "")
    private String field3;
	
	@ScreenField(row = 11, column = 29, endColumn= 33 ,displayName = "")
    private String field2;
	
	@ScreenField(row = 12, column = 29, endColumn= 33 ,displayName = "")
    private String field;
	
	@ScreenField(row = 13, column = 29, endColumn= 29 ,displayName = "Item Type  business area")
    private String itemTypeBusinessArea;
	
	@ScreenField(row = 14, column = 29, endColumn= 29 ,displayName = "Stock Analysis Code")
    private String stockAnalysisCode;
	
	@ScreenField(row = 15, column = 29, endColumn= 29 ,displayName = "Stock Value Group")
    private String stockValueGroup;
	
	@ScreenField(row = 16, column = 29, endColumn= 30 ,displayName = "Stock Inventory Group")
    private String stockInventoryGroup;
	
	@ScreenField(row = 17, column = 29, endColumn= 40 ,displayName = "List Price")
    private String listPrice;
	
	@ScreenField(row = 18, column = 29, endColumn= 41 ,displayName = "Standard unit cost")
    private String standardUnitCost;
	
	@ScreenField(row = 19, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Created date")
    private String createdDate;
	
	@ScreenField(row = 20, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Created By", sampleValue = "OPENLEGA1")
    private String createdBy;
	
	@ScreenField(row = 21, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Amended date")
    private String amendedDate;
	
	@ScreenField(row = 22, column = 69, endColumn= 78 ,labelColumn= 51 ,displayName = "Amended By", sampleValue = "OPENLEGA1")
    private String amendedBy;

	@ScreenField(endColumn = 42, row = 5, column = 23, labelColumn = 2, sampleValue = "1000", endRow = 5, displayName = "Item Number/Desc")
	private String itemNumber;


    


 
}
