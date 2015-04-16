// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect HoldJobhldjob_Aspect {

    declare parents: HoldJobhldjob implements ScreenEntity;
    private String HoldJobhldjob.focusField;
    private List<TerminalActionDefinition> HoldJobhldjob.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

    

    public String HoldJobhldjob.getJobName(){
    	return this.jobName;
    }
    

    public String HoldJobhldjob.getUser(){
    	return this.user;
    }
    

    public String HoldJobhldjob.getNumber(){
    	return this.number;
    }
    

    public Boolean HoldJobhldjob.getHoldSpooledFiles(){
    	return this.holdSpooledFiles;
    }
    
    public void HoldJobhldjob.setHoldSpooledFiles(Boolean holdSpooledFiles){
    	this.holdSpooledFiles = holdSpooledFiles;
    }


    public String HoldJobhldjob.getFocusField(){
    	return focusField;
    }
    public void HoldJobhldjob.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> HoldJobhldjob.getActions(){
    	return actions;
    }
    
}
