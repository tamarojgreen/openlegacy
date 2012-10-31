package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.ItemDetails;

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(endpointInterface = "org.openlegacy.terminal.samples.services.ItemDetailsWebService")
public class ItemDetailsWebServiceImpl implements ItemDetailsWebService {

	@Inject
	private TerminalSession terminalSession;

	@Override
	public ItemDetails getItem(int itemNumber) throws RegistryException, LoginException {
		terminalSession.getModule(Login.class).login("user", "pwd");
		return terminalSession.getEntity(ItemDetails.class, itemNumber);
	}
}
