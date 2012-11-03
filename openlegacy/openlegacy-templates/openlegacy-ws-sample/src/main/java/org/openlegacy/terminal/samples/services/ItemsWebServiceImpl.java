package org.openlegacy.terminal.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.Items;
import org.openlegacy.terminal.samples.model.Items.ItemsRecord;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "org.openlegacy.terminal.samples.services.ItemsWebService")
public class ItemsWebServiceImpl implements ItemsWebService {

	@Inject
	private TerminalSession terminalSession;

	@Resource
	private WebServiceContext webServiceContext;

	@Inject
	private UserDetailsService userDetailsService;

	@Override
	public List<ItemsRecord> getItems() throws RegistryException, LoginException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)webServiceContext.getUserPrincipal();
		UserDetails user = userDetailsService.loadUserByUsername(token.getName());

		terminalSession.getModule(Login.class).login(user.getUsername(), user.getPassword());
		return terminalSession.getModule(Table.class).collectAll(Items.class, ItemsRecord.class);
	}

}
