// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect WorkWithMlbAttributes_Aspect {

    declare parents: WorkWithMlbAttributes implements ScreenEntity;
    private String WorkWithMlbAttributes.focusField;
    private List<TerminalActionDefinition> WorkWithMlbAttributes.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

    

    public String WorkWithMlbAttributes.getJob(){
    	return this.job;
    }
    

    public String WorkWithMlbAttributes.getUser(){
    	return this.user;
    }
    

    public String WorkWithMlbAttributes.getNumber(){
    	return this.number;
    }
    

    public String WorkWithMlbAttributes.getCommand(){
    	return this.command;
    }
    
    public void WorkWithMlbAttributes.setCommand(String command){
    	this.command = command;
    }


    public String WorkWithMlbAttributes.getFocusField(){
    	return focusField;
    }
    public void WorkWithMlbAttributes.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> WorkWithMlbAttributes.getActions(){
    	return actions;
    }
    
}