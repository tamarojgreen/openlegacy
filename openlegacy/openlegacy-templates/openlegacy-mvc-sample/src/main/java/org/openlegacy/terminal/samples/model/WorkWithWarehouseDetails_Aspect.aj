// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.terminal.samples.model;

import java.util.*;
import org.openlegacy.terminal.ScreenEntity;

privileged @SuppressWarnings("unused") aspect WorkWithWarehouseDetails_Aspect {

    declare parents: WorkWithWarehouseDetails implements ScreenEntity;
    private String WorkWithWarehouseDetails.focusField;
    
	

	

    

    public String WorkWithWarehouseDetails.getPositionTo(){
    	return this.positionTo;
    }
    
    public void WorkWithWarehouseDetails.setPositionTo(String positionTo){
    	this.positionTo = positionTo;
    }



    public List<WorkWithWarehouseDetailsRecord> WorkWithWarehouseDetails.getWorkWithWarehouseDetailsRecords(){
    	return this.workWithWarehouseDetailsRecords;
    }
    




    public String WorkWithWarehouseDetails.getFocusField(){
    	return focusField;
    }
    public void WorkWithWarehouseDetails.setFocusField(String focusField){
    	this.focusField = focusField;
    }
    
}
