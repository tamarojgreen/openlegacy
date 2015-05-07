package com.openlegacy.ws.openlegacy.services;

import org.openlegacy.remoting.entities.Items;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * A service interface and input/oputput definition for a web service. Defines the contract between the client and server. The
 * client uses the same interface for testing the service via Java code. The interface ItemsService can be customized to enabling
 * passing parameters to the service.
 */

@WebService
public interface ItemsService {

	@WebMethod(operationName = "getItems")
	@WebResult(name = "ItemsOutput")
	public ItemsOut getItems();

	public static class ItemsOut {
		Items items;

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}
	}
}
