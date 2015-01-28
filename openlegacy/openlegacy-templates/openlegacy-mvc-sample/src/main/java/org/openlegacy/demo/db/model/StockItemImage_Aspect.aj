// WARNING: DO NOT EDIT THIS FILE.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.openlegacy.demo.db.model;

import java.io.Serializable;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;

privileged @SuppressWarnings("unused") aspect StockItemImage_Aspect {

	declare parents: StockItemImage implements Serializable;
	
	private static final long StockItemImage.serialVersionUID = 1L;
	


	public void StockItemImage.setId(Long id){
		this.id = id;
	}


	public void StockItemImage.setStockItem(StockItem stockItem){
		this.stockItem = stockItem;
	}

}