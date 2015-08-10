package org.openlegacy.ws;

import org.openlegacy.annotations.ws.Service;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.RpcSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.jws.WebService;

//import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 * A service implementation which invokes OpenLegacy API, and returns a service output. The code below should be customize to
 * perform a working scenario which goes through the relevant screens. Can be tested by invoking the test ItemDetailsServiceTest.
 * The interface ItemDetailsService can be customized to enabling passing parameters to the service, and this class can consume
 * the parameters within the relavant screens.
 */
@Service(name = "ItemDetailsServiceImpl")
@WebService(endpointInterface = "com.olwsrpcsample.openlegacy.services.ItemDetailsService")
public class ItemDetailsServiceImpl implements ItemDetailsService {

	@Inject
	@Qualifier("itemDetailsPool")
	private RpcSessionFactory rpcSessionFactory;

	@Override
	public ItemDetailsOut getItemDetails(ItemDetailsIn itemDetailsIn) {

		RpcSession rpcSession = rpcSessionFactory.getSession();
		try {
			Itemdetails itemdetails = rpcSession.getEntity(Itemdetails.class, itemDetailsIn.getItemNum());

			ItemDetailsOut itemDetailsOut = new ItemDetailsOut();
			itemDetailsOut.setItemdetails(itemdetails);

			return itemDetailsOut;
		} catch (RuntimeException e) {
			rpcSession.disconnect();
			throw (e);
		} finally {
			rpcSessionFactory.returnSession(rpcSession);
		}
	}

}
