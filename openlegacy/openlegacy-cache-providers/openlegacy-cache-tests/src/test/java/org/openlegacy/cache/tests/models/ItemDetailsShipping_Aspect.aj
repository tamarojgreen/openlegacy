// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.cache.tests.models;



privileged @SuppressWarnings("unused") aspect ItemDetailsShipping_Aspect {

	public String ItemDetails.Shipping.getShippingMethod() {
		return this.shippingMethod;
	}
	public Integer ItemDetails.Shipping.getDays() {
		return this.days;
	}

}