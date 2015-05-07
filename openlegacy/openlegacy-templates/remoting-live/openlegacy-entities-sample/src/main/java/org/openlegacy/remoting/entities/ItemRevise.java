package org.openlegacy.remoting.entities;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
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

import java.io.Serializable;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 6, column = 2, value = "Item Number . . . . . . . . ."),
		@Identifier(row = 7, column = 33, value = "true", attribute = FieldAttributeType.Editable),
		@Identifier(row = 6, column = 33, value = "false", attribute = FieldAttributeType.Editable) })
@ScreenActions(actions = { @Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = Items.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "act", value = "2") })
public class ItemRevise implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 6, column = 33, endColumn = 52, labelColumn = 2, displayName = "Item Number", sampleValue = "001-01")
	private String itemNumber;

	@ScreenField(row = 7, column = 33, endColumn = 72, labelColumn = 2, editable = true, displayName = "Item Description", sampleValue = "Pencils")
	private String itemDescription;

	@ScreenField(row = 8, column = 33, endColumn = 42, labelColumn = 2, editable = true, displayName = "Alpha Search", sampleValue = "PENCILS")
	private String alphaSearch;

	@ScreenField(row = 9, column = 33, endColumn = 52, labelColumn = 2, editable = true, displayName = "Superceding item To")
	private String supercedingItemto;

	@ScreenField(row = 10, column = 33, endColumn = 52, labelColumn = 2, editable = true, displayName = "Superceding Item From")
	private String supercedingItemfrom;

	@ScreenField(row = 11, column = 33, endColumn = 52, labelColumn = 2, editable = true, displayName = "Substitute item number")
	private String substituteItemNumber;

	@ScreenField(row = 12, column = 33, endColumn = 52, labelColumn = 2, editable = true, displayName = "Manufacturers Item No")
	private String manufacturersItemNo;

	@ScreenField(row = 13, column = 33, endColumn = 46, labelColumn = 2, editable = true, displayName = "Item weight", sampleValue = ".00")
	private String itemWeight;

	@ScreenField(row = 14, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Item Class", sampleValue = "CL1", helpText = "Class 1")
	private String itemClass;

	@ScreenField(row = 14, column = 37, endColumn = 76, displayName = "Item Class Name")
	private String itemClassName;

	@ScreenField(row = 15, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Stock Group", sampleValue = "G01", helpText = "Stationery")
	private String stockGroup;

	@ScreenField(row = 15, column = 37, endColumn = 76, displayName = "Stock Group Name")
	private String stockGroupName;

	@ScreenField(row = 16, column = 33, endColumn = 37, labelColumn = 2, editable = true, displayName = "Unit of Measure", sampleValue = "EA")
	private String unitOfMeasure;

	@ScreenField(row = 17, column = 33, endColumn = 37, labelColumn = 2, editable = true, displayName = "Packing Multiplier", sampleValue = "0")
	private Integer packingMultiplier;

	@ScreenField(row = 18, column = 33, endColumn = 37, labelColumn = 2, editable = true, displayName = "Outer Unit of Measure", sampleValue = "EA")
	private String outerUnitOfMeasure;

	@ScreenField(row = 19, column = 33, endColumn = 41, labelColumn = 2, editable = true, displayName = "Outer Quantity", sampleValue = "0")
	private Integer outerQuantity;

	@ScreenField(row = 20, column = 33, endColumn = 33, labelColumn = 2, editable = true, displayName = "Pallet label required")
	private String palletLabelRequired;

	@ScreenField(row = 21, column = 33, endColumn = 34, labelColumn = 2, editable = true, displayName = "VAT Code")
	private String vatCode;

	private ItemReviseStock itemReviseStock;

}
