package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.modules.menu.Menu.MenuEntity;  
import org.openlegacy.modules.menu.Menu.MenuSelectionField;  

@ScreenEntity(screenType=MenuEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 16, value = "               Inventory Menu               "), 
				@Identifier(row = 20, column = 2, value = "Selection:") 
				})
@ScreenNavigation(accessedFrom = MainMenu.class 
					, assignedFields = { 
					@AssignedField(field = "menuSelection", value = "1")
					 }						
					)
public class InventoryMenu {
    
	
	@ScreenField(row = 21, column = 8, endColumn= 9 ,editable= true ,fieldType=MenuSelectionField.class ,displayName = "Menu Selection")
    private Integer menuSelection;


    


 
}
