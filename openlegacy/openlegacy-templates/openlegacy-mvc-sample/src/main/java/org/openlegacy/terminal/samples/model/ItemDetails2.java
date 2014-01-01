package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity(child = true, displayName = "Item Stock details")
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 4, column = 2, value = "Type choices, press Enter."),
		@Identifier(row = 6, column = 2, value = "Item Number/Desc :") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F2.class, additionalKey = AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") })
@ScreenNavigation(accessedFrom = ItemDetails.class, assignedFields = { @AssignedField(field = "itemDescription") })
public class ItemDetails2 {

	@ScreenField(key = true, row = 6, column = 22, endColumn = 36, labelColumn = 2, displayName = "Item Number/Desc", sampleValue = "2000")
	private Integer itemNumberdesc;

	@ScreenField(row = 7, column = 29, endColumn = 43, labelColumn = 2, editable = true, displayName = "N/L Cost of Sales Account")
	private String nlCostOfSalesAccount;

	@ScreenField(row = 8, column = 29, endColumn = 43, labelColumn = 2, editable = true, displayName = "N/L Sales Account")
	private String nlSalesAccount;

	@ScreenField(row = 9, column = 29, endColumn = 43, labelColumn = 2, editable = true, displayName = "N/L Stock Account")
	private String nlStockAccount;

	@ScreenField(row = 10, column = 29, endColumn = 33, labelColumn = 18, editable = true)
	private String field2;

	@ScreenField(row = 11, column = 29, endColumn = 33, labelColumn = 18, editable = true)
	private String field1;

	@ScreenField(row = 12, column = 29, endColumn = 33, labelColumn = 18, editable = true)
	private String field;

	@ScreenField(row = 13, column = 29, endColumn = 29, labelColumn = 2, editable = true, displayName = "Item Type  business area")
	private String itemTypeBusinessArea;

	@ScreenField(row = 14, column = 29, endColumn = 29, labelColumn = 2, editable = true, displayName = "Stock Analysis Code")
	private String stockAnalysisCode;

	@ScreenField(row = 15, column = 29, endColumn = 29, labelColumn = 2, editable = true, displayName = "Stock Value Group")
	private String stockValueGroup;

	@ScreenField(row = 16, column = 29, endColumn = 30, labelColumn = 2, editable = true, displayName = "Stock Inventory Group")
	private String stockInventoryGroup;

	@ScreenField(row = 17, column = 29, endColumn = 41, labelColumn = 2, editable = true, displayName = "List Price")
	private Integer listPrice;

	@ScreenField(row = 18, column = 29, endColumn = 41, labelColumn = 2, editable = true, displayName = "Standard unit cost")
	private Integer standardUnitCost;

	private StockInfo stockInfo;

	@ScreenPart
	public static class StockInfo {

		@ScreenField(row = 19, column = 69, endColumn = 78, labelColumn = 51, displayName = "Created date", sampleValue = "17/01/2010")
		private String createdDate;

		@ScreenField(row = 20, column = 69, endColumn = 78, labelColumn = 51, displayName = "Created By", sampleValue = "TESTUSER")
		private String createdBy;

		@ScreenField(row = 21, column = 69, endColumn = 78, labelColumn = 51, displayName = "Amended date", sampleValue = "0/00/0000")
		private String amendedDate;

	}
}
