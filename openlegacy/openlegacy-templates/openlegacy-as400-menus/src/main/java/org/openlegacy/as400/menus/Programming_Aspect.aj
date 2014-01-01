// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect Programming_Aspect {

    declare parents: Programming implements ScreenEntity;
    private String Programming.focusField;
    private List<TerminalActionDefinition> Programming.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

    

    public String Programming.getSystem(){
    	return this.system;
    }
    



    public String Programming.getMenuSelection(){
    	return this.menuSelection;
    }
    
    public void Programming.setMenuSelection(String menuSelection){
    	this.menuSelection = menuSelection;
    }




    public String Programming.getFocusField(){
    	return focusField;
    }
    public void Programming.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> Programming.getActions(){
    	return actions;
    }
    
}
