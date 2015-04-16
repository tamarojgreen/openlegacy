// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect DisplayCallStack_Aspect {

    declare parents: DisplayCallStack implements ScreenEntity;
    private String DisplayCallStack.focusField;
    private List<TerminalActionDefinition> DisplayCallStack.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

	

    private List<TerminalActionDefinition> DisplayCallStack.displayCallStackRecordsActions = new ArrayList<TerminalActionDefinition>();
    

    public String DisplayCallStack.getJob(){
    	return this.job;
    }
    

    public String DisplayCallStack.getUser(){
    	return this.user;
    }
    

    public String DisplayCallStack.getNumber(){
    	return this.number;
    }
    

    public String DisplayCallStack.getThread(){
    	return this.thread;
    }
    

    public List<DisplayCallStackRecord> DisplayCallStack.getDisplayCallStackRecords(){
    	return this.displayCallStackRecords;
    }
    

    public List<TerminalActionDefinition> DisplayCallStack.getDisplayCallStackRecordsActions(){
    	return this.displayCallStackRecordsActions;
    }

    public String DisplayCallStack.getFocusField(){
    	return focusField;
    }
    public void DisplayCallStack.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> DisplayCallStack.getActions(){
    	return actions;
    }
    
}
