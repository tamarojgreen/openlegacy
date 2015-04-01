package com.openlegacyrestsample.openlegacy;

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

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 6, column = 2, value = "Item Number . . . . . . . . ."),
		@Identifier(row = 7, column = 33, value = "false", attribute = FieldAttributeType.Editable) })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = Items.class, exitAction = F12.class, assignedFields = { @AssignedField(field = "act", value = "5") })
public class ItemDetails {
    
	
	@ScreenField(row = 6, column = 33, endColumn= 52 ,labelColumn= 2 ,displayName = "Item Number", sampleValue = "001-01")
    private String itemNumber;
	
	@ScreenField(row = 7, column = 33, endColumn= 72 ,displayName = "Item Description")
    private String itemDescription;
	
	@ScreenField(row = 8, column = 33, endColumn= 42 ,labelColumn= 2 ,displayName = "Alpha Search", sampleValue = "PENCILS")
    private String alphaSearch;
	
	@ScreenField(row = 9, column = 33, endColumn= 52 ,displayName = "Superceding item To")
    private String supercedingItemto;
	
	@ScreenField(row = 10, column = 33, endColumn= 52 ,displayName = "Superceding Item From")
    private String supercedingItemfrom;
	
	@ScreenField(row = 11, column = 33, endColumn= 52 ,displayName = "Substitute item number")
    private String substituteItemNumber;
	
	@ScreenField(row = 12, column = 33, endColumn= 52 ,displayName = "Manufacturers Item No")
    private String manufacturersItemNo;
	
	@ScreenField(row = 13, column = 33, endColumn= 46 ,displayName = "Item weight")
    private String itemWeight;
	
	@ScreenField(row = 14, column = 33, endColumn= 35 ,labelColumn= 2 ,displayName = "Item Class", sampleValue = "CL1")
    private String itemClass;
	
	@ScreenField(row = 14, column = 37, endColumn = 76, displayName = "Item Class Name")
    private String itemClassName;
	
	@ScreenField(row = 15, column = 33, endColumn= 35 ,labelColumn= 2 ,displayName = "Stock Group", sampleValue = "G01")
    private String stockGroup;
	
	@ScreenField(row = 15, column = 37, endColumn = 76, displayName = "Stock Group Name")
    private String stockGroupName;
	
	@ScreenField(row = 16, column = 33, endColumn= 37 ,labelColumn= 2 ,displayName = "Unit of Measure", sampleValue = "EA")
    private String unitOfMeasure;
	
	@ScreenField(row = 17, column = 33, endColumn = 37, labelColumn = 2, displayName = "Packing Multiplier", sampleValue = "0")
    private String packingMultiplier;
	
	@ScreenField(row = 18, column = 33, endColumn= 37 ,labelColumn= 2 ,displayName = "Outer Unit of Measure", sampleValue = "EA")
    private String outerUnitOfMeasure;
	
	@ScreenField(row = 19, column = 33, endColumn= 41 ,labelColumn= 2 ,displayName = "Outer Quantity", sampleValue = "0")
    private String outerQuantity;
	
	@ScreenField(row = 20, column = 33, endColumn= 33 ,displayName = "Pallet label required")
    private String palletLabelRequired;
	
	@ScreenField(row = 21, column = 33, endColumn= 34 ,displayName = "VAT Code")
    private String vatCode;


    


 
}
