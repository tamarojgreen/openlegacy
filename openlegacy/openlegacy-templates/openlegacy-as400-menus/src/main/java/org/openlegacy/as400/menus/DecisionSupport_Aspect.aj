// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect DecisionSupport_Aspect {

    declare parents: DecisionSupport implements ScreenEntity;
    private String DecisionSupport.focusField;
    private List<TerminalActionDefinition> DecisionSupport.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

    

    public String DecisionSupport.getSystem(){
    	return this.system;
    }
    



    public String DecisionSupport.getMenuSelection(){
    	return this.menuSelection;
    }
    
    public void DecisionSupport.setMenuSelection(String menuSelection){
    	this.menuSelection = menuSelection;
    }




    public String DecisionSupport.getFocusField(){
    	return focusField;
    }
    public void DecisionSupport.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> DecisionSupport.getActions(){
    	return actions;
    }
    
}
