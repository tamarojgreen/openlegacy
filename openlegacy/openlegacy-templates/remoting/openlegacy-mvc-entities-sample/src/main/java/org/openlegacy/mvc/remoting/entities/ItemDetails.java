package org.openlegacy.mvc.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.mvc.remoting.entities.ItemDetails.ItemDetailsDrilldownAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.EnterDrilldownAction;

import java.io.Serializable;

@ScreenEntity(displayName = "Main Item Details")
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 4, column = 2, value = "Type choices, press Enter."),
		@Identifier(row = 6, column = 2, value = "Item Number . . . . . . . . .") })
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, additionalKey = AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") })
@ScreenNavigation(accessedFrom = Items.class, requiresParameters = true, terminalAction = ItemDetailsDrilldownAction.class)
public class ItemDetails {

	private static final long serialVersionUID = 1L;

	@ScreenField(key = true, row = 6, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Item Number", sampleValue = "2000")
	private Integer itemNumber;

	@ScreenField(row = 7, column = 33, endColumn = 72, labelColumn = 2, editable = true, displayName = "Item Description")
	private String itemDescription;

	@ScreenField(row = 8, column = 33, endColumn = 42, labelColumn = 2, editable = true, displayName = "Alpha Search")
	private String alphaSearch;

	@ScreenField(row = 10, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Superceding Item From")
	private String supercedingItemfrom;

	@ScreenField(row = 9, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Superceding item To")
	private String supercedingItemto;

	@ScreenField(row = 11, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Substitute item number")
	private String substituteItemNumber;

	@ScreenField(row = 12, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Manufacturers Item No")
	private String manufacturersItemNo;

	@ScreenField(row = 15, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Stock Group", sampleValue = "SG")
	private StockGroup stockGroup;

	@ScreenField(row = 13, column = 33, endColumn = 47, labelColumn = 2, editable = true, displayName = "Item weight")
	private Integer itemWeight;

	@ScreenBooleanField(trueValue = "1", falseValue = "")
	@ScreenField(row = 20, column = 33, endColumn = 33, labelColumn = 2, editable = true, displayName = "Pallet label required")
	private Boolean palletLabelRequired;

	private ItemDetails2 itemDetails2;

	public enum StockGroup {
		StandardStockGroup("SG", "Standard Stock Group"),
		CustomStockGroup("CG", "Custom Stock Group");

		private String value;
		private String display;

		StockGroup(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	public static class ItemDetailsDrilldownAction extends EnterDrilldownAction implements Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public Object getActionValue() {
			return 2;
		}
	}

}
