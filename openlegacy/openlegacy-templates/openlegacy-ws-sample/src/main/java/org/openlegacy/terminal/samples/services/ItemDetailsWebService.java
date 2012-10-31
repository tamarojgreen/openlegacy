package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.samples.model.ItemDetails;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface ItemDetailsWebService {

	@WebMethod(operationName = "getItem")
	@WebResult(name = "ItemDetails")
	public ItemDetails getItem(@WebParam(name = "itemNumber") int itemNumber) throws RegistryException, LoginException;

}
