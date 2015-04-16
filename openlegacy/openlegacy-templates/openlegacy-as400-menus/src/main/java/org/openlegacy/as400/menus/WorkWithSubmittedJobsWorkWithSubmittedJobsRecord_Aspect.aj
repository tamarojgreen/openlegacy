// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.as400.menus;

import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.as400.menus.WorkWithSubmittedJobs.WorkWithSubmittedJobsRecord;

privileged @SuppressWarnings("unused") aspect WorkWithSubmittedJobsRecord_Aspect {

	
    
    private String WorkWithSubmittedJobsRecord.focusField;
	
    public String WorkWithSubmittedJobsRecord.getOpt(){
    	return this.opt;
    }
    

    public String WorkWithSubmittedJobsRecord.getJob(){
    	return this.job;
    }
    


    /**
    	Focus on the given field, or on the first field in the table if none is given
    */
    public void WorkWithSubmittedJobsRecord.focus(String... field) {
        if (field.length > 0) {
            this.focusField = field[0];
        } else {
            this.focusField = "opt";
        }
    }

    public String WorkWithSubmittedJobsRecord.getFocus() {
        return focusField;
    }
    public void WorkWithSubmittedJobsRecord.setFocus(String focus) {
        this.focusField = focus;
    }
		
    public int WorkWithSubmittedJobsRecord.hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean WorkWithSubmittedJobsRecord.equals(Object other){
    	// TODO exclude terminal fields
		return EqualsBuilder.reflectionEquals(this,other);
    }
}