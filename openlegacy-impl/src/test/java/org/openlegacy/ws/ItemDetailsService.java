package org.openlegacy.ws;

import org.openlegacy.annotations.ws.ServiceMethod;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * A service interface and input/oputput definition for a web service. Defines the contract between the client and server. The
 * client uses the same interface for testing the service via Java code. The interface ItemDetailsService can be customized to
 * enabling passing parameters to the service.
 */

@WebService
public interface ItemDetailsService {

	@ServiceMethod(name = "getItemDetails", cacheDuration = 0)
	@WebMethod(operationName = "getItemDetails")
	@WebResult(name = "ItemDetailsOutput")
	public ItemDetailsOut getItemDetails(ItemDetailsIn itemDetailsIn);

	public static class ItemDetailsIn {

		Integer itemNum;

		public Integer getItemNum() {
			return itemNum;
		}

		public void setItemNum(Integer itemNum) {
			this.itemNum = itemNum;
		}

	}

	public static class ItemDetailsOut {

		Itemdetails itemdetails;

		public Itemdetails getItemdetails() {
			return itemdetails;
		}

		public void setItemdetails(Itemdetails itemdetails) {
			this.itemdetails = itemdetails;
		}
	}
}
