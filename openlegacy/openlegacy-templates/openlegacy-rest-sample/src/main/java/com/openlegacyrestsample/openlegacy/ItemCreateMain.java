package com.openlegacyrestsample.openlegacy;

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
import org.openlegacy.terminal.actions.TerminalActions.F6;

@ScreenEntity(displayName = "Item Create")
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 2, column = 26, value = "    Work with Item Master     "),
		@Identifier(row = 6, column = 33, value = "true", attribute = FieldAttributeType.Editable),
		@Identifier(row = 7, column = 2, value = "Item Description  . . . . . .") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F2.class, displayName = "Delete", alias = "delete", additionalKey = AdditionalKey.SHIFT) })
@ScreenNavigation(accessedFrom = Items.class, terminalAction = F6.class, exitAction = F12.class)
public class ItemCreateMain {
    
	
	@ScreenField(key = true, row = 6, column = 33, endColumn = 52, labelColumn = 2, editable = true, displayName = "Item Number")
    private String itemNumber;
	
	@ScreenField(row = 7, column = 33, endColumn= 72 ,labelColumn= 2 ,editable= true ,displayName = "Item Description")
    private String itemDescription;
	
	@ScreenField(row = 8, column = 33, endColumn= 42 ,labelColumn= 2 ,editable= true ,displayName = "Alpha Search")
    private String alphaSearch;
	
	@ScreenField(row = 9, column = 33, endColumn= 52 ,labelColumn= 2 ,editable= true ,displayName = "Superceding item To")
    private String supercedingItemto;
	
	@ScreenField(row = 10, column = 33, endColumn= 52 ,labelColumn= 2 ,editable= true ,displayName = "Superceding Item From")
    private String supercedingItemfrom;
	
	@ScreenField(row = 11, column = 33, endColumn= 52 ,labelColumn= 2 ,editable= true ,displayName = "Substitute item number")
    private String substituteItemNumber;
	
	@ScreenField(row = 12, column = 33, endColumn= 52 ,labelColumn= 2 ,editable= true ,displayName = "Manufacturers Item No")
    private String manufacturersItemNo;
	
	@ScreenField(row = 14, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Item Class", enableLookup = true)
    private String itemClass;
	
	@ScreenField(row = 15, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Stock Group", enableLookup = true)
    private String stockGroup;
	
	@ScreenField(row = 16, column = 33, endColumn= 37 ,labelColumn= 2 ,editable= true ,displayName = "Unit of Measure")
    private String unitOfMeasure;
	
	@ScreenField(row = 17, column = 33, endColumn= 37 ,labelColumn= 2 ,editable= true ,displayName = "Packing Multiplier", sampleValue = "0")
    private Integer packingMultiplier;
	
	@ScreenField(row = 18, column = 33, endColumn= 37 ,labelColumn= 2 ,editable= true ,displayName = "Outer Unit of Measure")
    private String outerUnitOfMeasure;
	
	@ScreenField(row = 19, column = 33, endColumn= 41 ,labelColumn= 2 ,editable= true ,displayName = "Outer Quantity", sampleValue = "0")
    private Integer outerQuantity;
	
	@ScreenField(row = 20, column = 33, endColumn= 33 ,labelColumn= 2 ,editable= true ,displayName = "Pallet label required")
    private String palletLabelRequired;
	
	@ScreenField(row = 21, column = 33, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "VAT Code")
    private String vatCode;

	@ScreenField(endColumn = 46, row = 13, column = 33, labelColumn = 2, sampleValue = ".00", editable = true, endRow = 13, displayName = "Item weight")
	private String itemWeight;

	@ScreenField(row = 15, column = 37, endColumn = 76, displayName = "Stock Group Name", sampleValue = "                                        ")
	private String stockGroupName;

	@ScreenField(row = 14, column = 37, endColumn = 76, displayName = "Item Class Name", sampleValue = "                                        ")
	private String itemClassName;


    


 
}
