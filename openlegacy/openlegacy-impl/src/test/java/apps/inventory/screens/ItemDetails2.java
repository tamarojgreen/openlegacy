package apps.inventory.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number/Desc :"),
		@Identifier(row = 7, column = 2, value = "N/L Cost of Sales Account") })
@ScreenNavigation(accessedFrom = ItemDetails1.class, exitAction = TerminalActions.F3.class)
public class ItemDetails2 {

	@FieldMapping(row = 6, column = 22)
	private String itemNumber;

	private AuditDetails auditDetails;

	private StockInfo stockInfo;

	@ScreenPart(supportTerminalData = true)
	public static class AuditDetails {

		@FieldMapping(row = 19, column = 69)
		private String createdDate;

		@FieldMapping(row = 20, column = 69)
		private String createdBy;

	}

	@ScreenPart
	public static class StockInfo {

		@FieldMapping(row = 17, column = 29, editable = true)
		private String listPrice;

		@FieldMapping(row = 18, column = 29, editable = true)
		private String standardUnitCost;

	}

}
