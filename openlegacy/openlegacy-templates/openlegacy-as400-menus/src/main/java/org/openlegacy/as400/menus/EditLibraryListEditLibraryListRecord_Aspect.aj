// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.as400.menus.EditLibraryList.EditLibraryListRecord;

privileged @SuppressWarnings("unused") aspect EditLibraryListRecord_Aspect {

	
    
    private String EditLibraryListRecord.focusField;
	
    public Integer EditLibraryListRecord.getNumber(){
    	return this.number;
    }
    
    public void EditLibraryListRecord.setNumber(Integer number){
    	this.number = number;
    }

    public String EditLibraryListRecord.getLibrary(){
    	return this.library;
    }
    
    public void EditLibraryListRecord.setLibrary(String library){
    	this.library = library;
    }

    public Integer EditLibraryListRecord.getNumber1(){
    	return this.number1;
    }
    
    public void EditLibraryListRecord.setNumber1(Integer number1){
    	this.number1 = number1;
    }

    public String EditLibraryListRecord.getLibrary1(){
    	return this.library1;
    }
    
    public void EditLibraryListRecord.setLibrary1(String library1){
    	this.library1 = library1;
    }

    public Integer EditLibraryListRecord.getNumber2(){
    	return this.number2;
    }
    
    public void EditLibraryListRecord.setNumber2(Integer number2){
    	this.number2 = number2;
    }

    public String EditLibraryListRecord.getLibrary2(){
    	return this.library2;
    }
    
    public void EditLibraryListRecord.setLibrary2(String library2){
    	this.library2 = library2;
    }


    /**
    	Focus on the given field, or on the first field in the table if none is given
    */
    public void EditLibraryListRecord.focus(String... field) {
        if (field.length > 0) {
            this.focusField = field[0];
        } else {
            this.focusField = "number";
        }
    }

    public String EditLibraryListRecord.getFocus() {
        return focusField;
    }
    public void EditLibraryListRecord.setFocus(String focus) {
        this.focusField = focus;
    }
		
    public int EditLibraryListRecord.hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean EditLibraryListRecord.equals(Object other){
    	// TODO exclude terminal fields
		return EqualsBuilder.reflectionEquals(this,other);
    }
}