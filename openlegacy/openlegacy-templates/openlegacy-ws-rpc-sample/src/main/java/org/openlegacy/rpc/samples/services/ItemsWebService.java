package org.openlegacy.rpc.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.rpc.samples.model.Items.InnerRecord;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface ItemsWebService {

	@WebMethod(operationName = "getItems")
	@WebResult(name = "List")
	public List<InnerRecord> getItems() throws RegistryException, LoginException;

}
