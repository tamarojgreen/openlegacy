package apps.inventory.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPartEntity;
import org.openlegacy.terminal.actions.SendKeyClasses;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number/Desc :"),
		@Identifier(row = 7, column = 2, value = "N/L Cost of Sales Account") })
@ScreenNavigation(accessedFrom = ItemDetails1.class, exitAction = SendKeyClasses.F3.class)
public class ItemDetails2 {

	@FieldMapping(row = 6, column = 22)
	private String itemNumber;

	private AuditDetails auditDetails;

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	@Component
	@Scope("prototype")
	@ScreenPartEntity
	public static class AuditDetails {

		@FieldMapping(row = 19, column = 69)
		private String createdDate;

		@FieldMapping(row = 20, column = 69)
		private String createdBy;

		public String getCreatedBy() {
			return createdBy;
		}

		public String getCreatedDate() {
			return createdDate;
		}

	}
}
