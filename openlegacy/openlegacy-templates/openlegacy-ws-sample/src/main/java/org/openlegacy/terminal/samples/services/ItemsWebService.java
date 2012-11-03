package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.samples.model.Items.ItemsRecord;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface ItemsWebService {

	@WebMethod(operationName = "getItems")
	@WebResult(name = "List")
	public List<ItemsRecord> getItems() throws RegistryException, LoginException;

}
