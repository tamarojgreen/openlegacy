// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect WorkWithPrinterOutput_Aspect {

    declare parents: WorkWithPrinterOutput implements ScreenEntity;
    private String WorkWithPrinterOutput.focusField;
    private List<TerminalActionDefinition> WorkWithPrinterOutput.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

    

    public String WorkWithPrinterOutput.getSystem(){
    	return this.system;
    }
    



    public String WorkWithPrinterOutput.getUser(){
    	return this.user;
    }
    



    public List<WorkWithPrinterOutputRecord> WorkWithPrinterOutput.getWorkWithPrinterOutputRecords(){
    	return this.workWithPrinterOutputRecords;
    }
    




    public String WorkWithPrinterOutput.getFocusField(){
    	return focusField;
    }
    public void WorkWithPrinterOutput.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> WorkWithPrinterOutput.getActions(){
    	return actions;
    }
    
}
