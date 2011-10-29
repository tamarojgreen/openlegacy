// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.someorg.examples.screens;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.CursorContainer;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

privileged aspect ItemsList_Aspect {
    
    declare @type: ItemsList : @Component;
	declare @type: ItemsList : @Scope("prototype");
    
    private TerminalScreen ItemsList.terminalScreen;

    declare parents: ItemsList implements ScreenEntity;
    declare parents: ItemsList implements CursorContainer;

    private ScreenPosition ItemsList.cursorPosition;
    
	
    private TerminalField ItemsList.PositionToField;
    
    public TerminalScreen ItemsList.getTerminalScreen(){
		return terminalScreen;
    }

    public String ItemsList.getPositionTo(){
    	return this.PositionTo;
    }
    

    public TerminalField ItemsList.getPositionToField(){
    	return PositionToField;
    }

    public ScreenPosition ItemsList.getCursorPosition(){
    	return cursorPosition;
    }
    public void ItemsList.setCursorPosition(ScreenPosition cursorPosition){
    	this.cursorPosition = cursorPosition;
    }
    
}
