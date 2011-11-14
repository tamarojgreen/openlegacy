// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package apps.inventory.screens;

import java.util.List;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

privileged @SuppressWarnings("unused") aspect SignOn_Aspect {
    
    declare @type: SignOn : @Component;
	declare @type: SignOn : @Scope("prototype");
    
    private TerminalScreen SignOn.terminalScreen;

    declare parents: SignOn implements ScreenEntity;
    private String SignOn.focusField;
    
	
    private TerminalField SignOn.errorField;
	
    private TerminalField SignOn.passwordField;
	
    private TerminalField SignOn.programProcedureField;
	
    private TerminalField SignOn.userField;
    
    public TerminalScreen SignOn.getTerminalScreen(){
		return terminalScreen;
    }

    public String SignOn.getError(){
    	return this.error;
    }
    

    public TerminalField SignOn.getErrorField(){
    	return errorField;
    }
    public String SignOn.getPassword(){
    	return this.password;
    }
    
    public void SignOn.setPassword(String password){
    	this.password = password;
    }

    public TerminalField SignOn.getPasswordField(){
    	return passwordField;
    }
    public String SignOn.getProgramProcedure(){
    	return this.programProcedure;
    }
    
    public void SignOn.setProgramProcedure(String programProcedure){
    	this.programProcedure = programProcedure;
    }

    public TerminalField SignOn.getProgramProcedureField(){
    	return programProcedureField;
    }
    public String SignOn.getUser(){
    	return this.user;
    }
    
    public void SignOn.setUser(String user){
    	this.user = user;
    }

    public TerminalField SignOn.getUserField(){
    	return userField;
    }

    public String SignOn.getFocusField(){
    	return focusField;
    }
    public void SignOn.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
}
