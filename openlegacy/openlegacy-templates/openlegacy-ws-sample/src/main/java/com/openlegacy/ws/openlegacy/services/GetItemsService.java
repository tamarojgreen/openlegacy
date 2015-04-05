package com.openlegacy.ws.openlegacy.services;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.openlegacy.ws.openlegacy.Items;
import com.openlegacy.ws.openlegacy.Items.ItemDetailesRecord;

/**
 *  A service interface and input/oputput definition for a web service.
 *  Defines the contract between the client and server. The client uses the same interface for testing the service via Java code. 
 *  The interface GetItemsService can be customized to enabling passing parameters to the service.     
 */

@WebService
public interface GetItemsService {

	@WebMethod(operationName = "getGetItems")
	@WebResult(name = "GetItemsOutput")
	public GetItemsOut getGetItems();

	public static class GetItemsOut{
		Items items;
		List<ItemDetailesRecord> records;
				
		public Items getItems(){
			return items;
		}
		
		public void setItems(Items items){
			this.items = items;
		}
		
		public List<ItemDetailesRecord> getRecords() {
			return records;
		}
		
		public void setRecords(List<ItemDetailesRecord> records2) {
			this.records = records2;
		}
	}
}
