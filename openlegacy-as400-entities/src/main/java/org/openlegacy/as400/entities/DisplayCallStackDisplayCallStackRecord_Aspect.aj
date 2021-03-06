// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.entities;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.as400.entities.DisplayCallStack.DisplayCallStackRecord;

privileged @SuppressWarnings("unused") aspect DisplayCallStackRecord_Aspect {

	
    
    private String DisplayCallStackRecord.focusField;
	
    public String DisplayCallStackRecord.getType(){
    	return this.type;
    }
    

    public String DisplayCallStackRecord.getProgram(){
    	return this.program;
    }
    

    public String DisplayCallStackRecord.getStatement(){
    	return this.statement;
    }
    

    public String DisplayCallStackRecord.getProcedure(){
    	return this.procedure;
    }
    


    /**
    	Focus on the given field, or on the first field in the table if none is given
    */
    public void DisplayCallStackRecord.focus(String... field) {
        if (field.length > 0) {
            this.focusField = field[0];
        } else {
            this.focusField = "type";
        }
    }

    public String DisplayCallStackRecord.getFocus() {
        return focusField;
    }
    public void DisplayCallStackRecord.setFocus(String focus) {
        this.focusField = focus;
    }
		
    public int DisplayCallStackRecord.hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean DisplayCallStackRecord.equals(Object other){
    	// TODO exclude terminal fields
		return EqualsBuilder.reflectionEquals(this,other);
    }
}