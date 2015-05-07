// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.remoting.entities;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.remoting.entities.ListOfWarehouseTypesLookUp.ListOfWarehouseTypesLookUpRecord;

privileged @SuppressWarnings("unused") aspect ListOfWarehouseTypesLookUpRecord_Aspect {

	
    
    private String ListOfWarehouseTypesLookUpRecord.focusField;
	
    public String ListOfWarehouseTypesLookUpRecord.getAction(){
    	return this.action;
    }
    
    public void ListOfWarehouseTypesLookUpRecord.setAction(String action){
    	this.action = action;
    }

    public String ListOfWarehouseTypesLookUpRecord.getType(){
    	return this.type;
    }
    

    public String ListOfWarehouseTypesLookUpRecord.getDescription(){
    	return this.description;
    }
    


    /**
    	Focus on the given field, or on the first field in the table if none is given
    */
    public void ListOfWarehouseTypesLookUpRecord.focus(String... field) {
        if (field.length > 0) {
            this.focusField = field[0];
        } else {
            this.focusField = "action";
        }
    }

    public String ListOfWarehouseTypesLookUpRecord.getFocus() {
        return focusField;
    }
    public void ListOfWarehouseTypesLookUpRecord.setFocus(String focus) {
        this.focusField = focus;
    }
		
    public int ListOfWarehouseTypesLookUpRecord.hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean ListOfWarehouseTypesLookUpRecord.equals(Object other){
    	// TODO exclude terminal fields
		return EqualsBuilder.reflectionEquals(this,other);
    }
}