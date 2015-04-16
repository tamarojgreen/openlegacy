// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect WorkWithJobMenu_Aspect {

    declare parents: WorkWithJobMenu implements ScreenEntity;
    private String WorkWithJobMenu.focusField;
    private List<TerminalActionDefinition> WorkWithJobMenu.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

    

    public String WorkWithJobMenu.getJob(){
    	return this.job;
    }
    

    public String WorkWithJobMenu.getUser(){
    	return this.user;
    }
    

    public String WorkWithJobMenu.getMenuSelection(){
    	return this.menuSelection;
    }
    
    public void WorkWithJobMenu.setMenuSelection(String menuSelection){
    	this.menuSelection = menuSelection;
    }

    public String WorkWithJobMenu.getNumber(){
    	return this.number;
    }
    


    public String WorkWithJobMenu.getFocusField(){
    	return focusField;
    }
    public void WorkWithJobMenu.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> WorkWithJobMenu.getActions(){
    	return actions;
    }
    
}
