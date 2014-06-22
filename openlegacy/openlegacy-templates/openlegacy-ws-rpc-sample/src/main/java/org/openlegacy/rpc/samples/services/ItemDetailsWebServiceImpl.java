package org.openlegacy.rpc.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.ItemDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "org.openlegacy.rpc.samples.services.ItemDetailsWebService")
public class ItemDetailsWebServiceImpl implements ItemDetailsWebService {

	@Inject
	private RpcSession rpcSession;

	@Resource
	private WebServiceContext webServiceContext;

	@Inject
	private UserDetailsService userDetailsService;

	@Override
	public ItemDetails getItem(int itemNumber) throws RegistryException, LoginException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)webServiceContext.getUserPrincipal();
		UserDetails user = userDetailsService.loadUserByUsername(token.getName());

		rpcSession.getModule(Login.class).login(user.getUsername(), user.getPassword());
		return rpcSession.getEntity(ItemDetails.class, itemNumber);
	}
}
