// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package apps.inventory.screens;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect ItemsList_Aspect {

    declare parents: ItemsList implements ScreenEntity;
    private String ItemsList.focusField;
    private List<TerminalActionDefinition> ItemsList.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

    private List<TerminalActionDefinition> ItemsList.itemsListRowsActions = new ArrayList<TerminalActionDefinition>();
	

    

    public String ItemsList.getPositionTo(){
    	return this.positionTo;
    }
    
    public void ItemsList.setPositionTo(String positionTo){
    	this.positionTo = positionTo;
    }

    public List<ItemsListRow> ItemsList.getItemsListRows(){
    	return this.itemsListRows;
    }
    

    public List<TerminalActionDefinition> ItemsList.getItemsListRowsActions(){
    	return this.itemsListRowsActions;
    }
    public String ItemsList.getErrorMessage(){
    	return this.errorMessage;
    }
    


    public String ItemsList.getFocusField(){
    	return focusField;
    }
    public void ItemsList.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> ItemsList.getActions(){
    	return actions;
    }
    
}
