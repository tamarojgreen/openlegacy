// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect WorkWithAllSpooledFiles_Aspect {

    declare parents: WorkWithAllSpooledFiles implements ScreenEntity;
    private String WorkWithAllSpooledFiles.focusField;
    private List<TerminalActionDefinition> WorkWithAllSpooledFiles.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

    private List<TerminalActionDefinition> WorkWithAllSpooledFiles.workWithAllSpooledFilesRecordsActions = new ArrayList<TerminalActionDefinition>();
    

    public String WorkWithAllSpooledFiles.getCommand(){
    	return this.command;
    }
    
    public void WorkWithAllSpooledFiles.setCommand(String command){
    	this.command = command;
    }

    public List<WorkWithAllSpooledFilesRecord> WorkWithAllSpooledFiles.getWorkWithAllSpooledFilesRecords(){
    	return this.workWithAllSpooledFilesRecords;
    }
    

    public List<TerminalActionDefinition> WorkWithAllSpooledFiles.getWorkWithAllSpooledFilesRecordsActions(){
    	return this.workWithAllSpooledFilesRecordsActions;
    }

    public String WorkWithAllSpooledFiles.getFocusField(){
    	return focusField;
    }
    public void WorkWithAllSpooledFiles.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> WorkWithAllSpooledFiles.getActions(){
    	return actions;
    }
    
}
