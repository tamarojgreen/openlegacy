package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "    Work with Item Master     "), 
				@Identifier(row = 4, column = 2, value = "Type choices, press Enter."), 
				@Identifier(row = 6, column = 2, value = "Item Number/Desc :") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F3.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Return", alias = "return"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = ItemDetails1.class, exitAction = TerminalActions.F3.class)
public class ItemDetails2 {

	@ScreenField(row = 6, column = 22 ,endColumn= 36 ,displayName = "Item NumberDesc", sampleValue = "2000")
    private String itemNumberdesc;

	@ScreenField(row = 7, column = 29 ,editable= true ,displayName = "NL Cost of Sales Account")
    private String nlCostOfSalesAccount;

	@ScreenField(row = 8, column = 29 ,editable= true ,displayName = "NL Sales Account")
    private String nlSalesAccount;

	@ScreenField(row = 9, column = 29 ,editable= true ,displayName = "NL Stock Account")
    private String nlStockAccount;

	@ScreenField(row = 13, column = 29 ,editable= true ,displayName = "Item Type  business area")
    private String itemTypeBusinessArea;

	@ScreenField(row = 14, column = 29 ,editable= true ,displayName = "Stock Analysis Code")
    private String stockAnalysisCode;

	@ScreenField(row = 15, column = 29 ,editable= true ,displayName = "Stock Value Group")
    private String stockValueGroup;

	@ScreenField(row = 16, column = 29 ,editable= true ,displayName = "Stock Inventory Group")
    private String stockInventoryGroup;

	private AuditDetails auditDetails;

	private StockInfo stockInfo;

	@ScreenPart(supportTerminalData = true)
	public static class AuditDetails {

		@ScreenField(row = 19, column = 69)
		private String createdDate;

		@ScreenField(row = 20, column = 69)
		private String createdBy;

	}

	@ScreenPart
	public static class StockInfo {

		@ScreenField(row = 17, column = 29, editable = true)
		private String listPrice;

		@ScreenField(row = 18, column = 29, editable = true)
		private String standardUnitCost;

	}

}
