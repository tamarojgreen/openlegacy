// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect UserTasksMenu_Aspect {

    declare parents: UserTasksMenu implements ScreenEntity;
    private String UserTasksMenu.focusField;
    private List<TerminalActionDefinition> UserTasksMenu.actions = new ArrayList<TerminalActionDefinition>();
    
	

    

    public String UserTasksMenu.getMenuSelection(){
    	return this.menuSelection;
    }
    
    public void UserTasksMenu.setMenuSelection(String menuSelection){
    	this.menuSelection = menuSelection;
    }


    public String UserTasksMenu.getFocusField(){
    	return focusField;
    }
    public void UserTasksMenu.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> UserTasksMenu.getActions(){
    	return actions;
    }
    
}
