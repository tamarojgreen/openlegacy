package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.ItemDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "org.openlegacy.terminal.samples.services.ItemDetailsWebService")
public class ItemDetailsWebServiceImpl implements ItemDetailsWebService {

	@Inject
	private TerminalSession terminalSession;

	@Resource
	private WebServiceContext webServiceContext;

	@Inject
	private UserDetailsService userDetailsService;

	@Override
	public ItemDetails getItem(int itemNumber) throws RegistryException, LoginException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)webServiceContext.getUserPrincipal();

		terminalSession.getModule(Login.class).login("user", "pwd");
		return terminalSession.getEntity(ItemDetails.class, itemNumber);
	}
}
