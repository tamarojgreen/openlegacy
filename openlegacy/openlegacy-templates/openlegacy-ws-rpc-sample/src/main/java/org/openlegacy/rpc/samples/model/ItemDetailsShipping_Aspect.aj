// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.rpc.samples.model;

import java.util.*;
import org.openlegacy.rpc.samples.model.ItemDetails.*;

privileged @SuppressWarnings("unused") aspect Shipping_Aspect {
    
    public String Shipping.getShippingMethod(){
    	return this.shippingMethod;
    }
    public void Shipping.setShippingMethod(String shippingMethod){
    	this.shippingMethod = shippingMethod;
    }
    public Integer Shipping.getDays(){
    	return this.days;
    }
    public void Shipping.setDays(Integer days){
    	this.days = days;
    }
    
}
