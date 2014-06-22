package org.openlegacy.rpc.samples.services;

import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.Items;
import org.openlegacy.rpc.samples.model.Items.InnerRecord;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "org.openlegacy.rpc.samples.services.ItemsWebService")
public class ItemsWebServiceImpl implements ItemsWebService {

	@Inject
	private RpcSession rpcSession;

	@Resource
	private WebServiceContext webServiceContext;

	@Inject
	private UserDetailsService userDetailsService;

	@Override
	public List<InnerRecord> getItems() throws RegistryException, LoginException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)webServiceContext.getUserPrincipal();
		UserDetails user = userDetailsService.loadUserByUsername(token.getName());

		rpcSession.getModule(Login.class).login(user.getUsername(), user.getPassword());
		return rpcSession.getEntity(Items.class).getInnerRecord();
	}

}
