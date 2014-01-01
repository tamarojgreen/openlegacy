package apps.inventory.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalActions.F3;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 38, value = "Warehouse Details"),
		@Identifier(row = 6, column = 2, value = "Warehouse Number .") })
@ScreenNavigation(accessedFrom = WarehousesList.class, exitAction = F3.class)
public class WarehouseDetails {

	@ScreenField(column = 34, row = 6, key = true)
	private Integer warehouseNumber;

	@ScreenField(column = 34, row = 7, editable = true)
	private String warehouseDescription;

	@ScreenField(column = 34, row = 9, editable = true)
	private Integer costingType;

	@ScreenField(column = 20, row = 11, editable = true)
	private String address;

	@ScreenField(column = 20, row = 12, editable = true)
	private String phone;

	@ScreenField(column = 20, row = 13, editable = true)
	private String email;

	@ScreenField(column = 34, row = 15, editable = true)
	private String amendedBy;

	@ScreenField(column = 34, row = 17, editable = true)
	private String createdBy;

	@ScreenField(column = 34, row = 8, editable = true)
	@ScreenFieldValues(sourceScreenEntity = WarehouseTypes.class, collectAll = false)
	private String warehouseType;
}
