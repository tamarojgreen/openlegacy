// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect DisplayJobDefinitionAttributes2_Aspect {

    declare parents: DisplayJobDefinitionAttributes2 implements ScreenEntity;
    private String DisplayJobDefinitionAttributes2.focusField;
    private List<TerminalActionDefinition> DisplayJobDefinitionAttributes2.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

	

	

	

	

	

	

	

	

	

	

	

	

    

    public String DisplayJobDefinitionAttributes2.getJob(){
    	return this.job;
    }
    

    public String DisplayJobDefinitionAttributes2.getUser(){
    	return this.user;
    }
    

    public Integer DisplayJobDefinitionAttributes2.getNumber(){
    	return this.number;
    }
    

    public String DisplayJobDefinitionAttributes2.getDefaultOutputQueue(){
    	return this.defaultOutputQueue;
    }
    

    public String DisplayJobDefinitionAttributes2.getDefaultOutputQueueLibary(){
    	return this.defaultOutputQueueLibary;
    }
    

    public String DisplayJobDefinitionAttributes2.getJobDate(){
    	return this.jobDate;
    }
    

    public String DisplayJobDefinitionAttributes2.getDateFormat(){
    	return this.dateFormat;
    }
    

    public String DisplayJobDefinitionAttributes2.getDateSeparator(){
    	return this.dateSeparator;
    }
    

    public String DisplayJobDefinitionAttributes2.getTimeSeparator(){
    	return this.timeSeparator;
    }
    

    public String DisplayJobDefinitionAttributes2.getCurrentDate(){
    	return this.currentDate;
    }
    

    public String DisplayJobDefinitionAttributes2.getCurrentTime(){
    	return this.currentTime;
    }
    

    public String DisplayJobDefinitionAttributes2.getTimeZone(){
    	return this.timeZone;
    }
    

    public String DisplayJobDefinitionAttributes2.getCurrentOffset(){
    	return this.currentOffset;
    }
    

    public String DisplayJobDefinitionAttributes2.getFullName(){
    	return this.fullName;
    }
    

    public String DisplayJobDefinitionAttributes2.getAbbreviatedName(){
    	return this.abbreviatedName;
    }
    

    public String DisplayJobDefinitionAttributes2.getDecimalFormat(){
    	return this.decimalFormat;
    }
    


    public String DisplayJobDefinitionAttributes2.getFocusField(){
    	return focusField;
    }
    public void DisplayJobDefinitionAttributes2.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> DisplayJobDefinitionAttributes2.getActions(){
    	return actions;
    }
    
}
