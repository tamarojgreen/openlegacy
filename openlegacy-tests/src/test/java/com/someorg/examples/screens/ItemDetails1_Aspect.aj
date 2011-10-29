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

privileged aspect ItemDetails1_Aspect {
    
    declare @type: ItemDetails1 : @Component;
	declare @type: ItemDetails1 : @Scope("prototype");
    
    private TerminalScreen ItemDetails1.terminalScreen;

    declare parents: ItemDetails1 implements ScreenEntity;
    declare parents: ItemDetails1 implements CursorContainer;

    private ScreenPosition ItemDetails1.cursorPosition;
    
	
    private TerminalField ItemDetails1.ItemNumberField;
	
    
    public TerminalScreen ItemDetails1.getTerminalScreen(){
		return terminalScreen;
    }

    public String ItemDetails1.getItemNumber(){
    	return this.ItemNumber;
    }
    

    public TerminalField ItemDetails1.getItemNumberField(){
    	return ItemNumberField;
    }
    public ItemDetails2 ItemDetails1.getItemDetails2(){
    	return this.itemDetails2;
    }
    


    public ScreenPosition ItemDetails1.getCursorPosition(){
    	return cursorPosition;
    }
    public void ItemDetails1.setCursorPosition(ScreenPosition cursorPosition){
    	this.cursorPosition = cursorPosition;
    }
    
}
