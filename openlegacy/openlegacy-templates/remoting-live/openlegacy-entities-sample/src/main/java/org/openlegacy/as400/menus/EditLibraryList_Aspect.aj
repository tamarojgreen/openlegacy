// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect EditLibraryList_Aspect {

    declare parents: EditLibraryList implements ScreenEntity;
    private String EditLibraryList.focusField;
    private List<TerminalActionDefinition> EditLibraryList.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

    private List<TerminalActionDefinition> EditLibraryList.editLibraryListRecordsActions = new ArrayList<TerminalActionDefinition>();
    

    public String EditLibraryList.getSystem(){
    	return this.system;
    }
    

    public List<EditLibraryListRecord> EditLibraryList.getEditLibraryListRecords(){
    	return this.editLibraryListRecords;
    }
    

    public List<TerminalActionDefinition> EditLibraryList.getEditLibraryListRecordsActions(){
    	return this.editLibraryListRecordsActions;
    }

    public String EditLibraryList.getFocusField(){
    	return focusField;
    }
    public void EditLibraryList.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> EditLibraryList.getActions(){
    	return actions;
    }
    
}
