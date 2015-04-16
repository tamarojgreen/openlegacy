// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

privileged @SuppressWarnings("unused") aspect ChangeJobchgjob_Aspect {

    declare parents: ChangeJobchgjob implements ScreenEntity;
    private String ChangeJobchgjob.focusField;
    private List<TerminalActionDefinition> ChangeJobchgjob.actions = new ArrayList<TerminalActionDefinition>();
    
	

	

	

	

	

	

	

	

	

    

    public String ChangeJobchgjob.getJobName(){
    	return this.jobName;
    }
    

    public String ChangeJobchgjob.getUser(){
    	return this.user;
    }
    

    public String ChangeJobchgjob.getNumber(){
    	return this.number;
    }
    

    public String ChangeJobchgjob.getPrintDevice(){
    	return this.printDevice;
    }
    
    public void ChangeJobchgjob.setPrintDevice(String printDevice){
    	this.printDevice = printDevice;
    }

    public String ChangeJobchgjob.getOutputQueue(){
    	return this.outputQueue;
    }
    
    public void ChangeJobchgjob.setOutputQueue(String outputQueue){
    	this.outputQueue = outputQueue;
    }

    public String ChangeJobchgjob.getLibrary(){
    	return this.library;
    }
    
    public void ChangeJobchgjob.setLibrary(String library){
    	this.library = library;
    }

    public Integer ChangeJobchgjob.getRunPriority(){
    	return this.runPriority;
    }
    
    public void ChangeJobchgjob.setRunPriority(Integer runPriority){
    	this.runPriority = runPriority;
    }

    public JobPriorityonJobq ChangeJobchgjob.getJobPriorityonJobq(){
    	return this.jobPriorityonJobq;
    }
    
    public void ChangeJobchgjob.setJobPriorityonJobq(JobPriorityonJobq jobPriorityonJobq){
    	this.jobPriorityonJobq = jobPriorityonJobq;
    }

    public OutputPriorityonOutq ChangeJobchgjob.getOutputPriorityonOutq(){
    	return this.outputPriorityonOutq;
    }
    
    public void ChangeJobchgjob.setOutputPriorityonOutq(OutputPriorityonOutq outputPriorityonOutq){
    	this.outputPriorityonOutq = outputPriorityonOutq;
    }


    public String ChangeJobchgjob.getFocusField(){
    	return focusField;
    }
    public void ChangeJobchgjob.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
    public List<TerminalActionDefinition> ChangeJobchgjob.getActions(){
    	return actions;
    }
    
}
