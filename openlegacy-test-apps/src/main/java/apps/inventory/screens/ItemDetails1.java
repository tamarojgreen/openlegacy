package apps.inventory.screens;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenDescriptionField;
import org.openlegacy.annotations.screen.ScreenDynamicField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.terminal.actions.TerminalActions;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 6, column = 2, value = "Item Number . ."),
		@Identifier(row = 7, column = 2, value = "Item Description") })
@ScreenActions(actions = { @Action(action = TerminalActions.F2.class, displayName = "Save", alias = "save") })
@ScreenNavigation(accessedFrom = ItemsList.class, exitAction = TerminalActions.F12.class)
public class ItemDetails1 {

	@ScreenField(row = 6, column = 33, key = true)
	@ScreenDynamicField(text = "Item Number", fieldOffset = 1, row = 5, endRow = 9, column = 1, endColumn = 70)
	private Integer itemNumber;

	@ScreenField(row = 7, column = 33, endColumn = 72, editable = true, roles = { "AGENT" })
	private String itemDescription;

	@ScreenField(row = 9, column = 33, editable = true)
	private String supercedingItemTo;

	@ScreenField(row = 10, column = 33, editable = true)
	private String supercedingItemFrom;

	@ScreenField(row = 13, column = 33, editable = true)
	private Long itemWeight;

	@ScreenDescriptionField(column = 37)
	@ScreenField(row = 14, column = 33, editable = true)
	private String itemClass;

	@ScreenDescriptionField(column = 37)
	@ScreenField(row = 15, column = 33, editable = true, sampleValue = "SG")
	private String stockGroup;

	@ScreenField(row = 17, column = 33, editable = true)
	private Integer packagingMultipler;

	@ScreenBooleanField(trueValue = "Y", falseValue = "N")
	@ScreenField(row = 20, column = 33, editable = true)
	private Boolean palletLabelRequired;

	@ScreenField(row = 18, column = 33, editable = true)
	private OuterUnitOfMeasure outerUnitOfMeasure;

	private ItemDetails2 itemDetails2;

	public enum OuterUnitOfMeasure implements EnumGetValue {
		Kilogram("kg", "kilogram"),
		Ton("tn", "ton");

		private String value;
		private String display;

		OuterUnitOfMeasure(String value, String display) {
			this.value = value;
			this.display = display;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}
}
