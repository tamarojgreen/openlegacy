package com.openlegacy.ws.openlegacy.services;

import org.openlegacy.terminal.ScreenEntity;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.inject.Inject;

import javax.jws.WebService;


import com.openlegacy.ws.openlegacy.Items; 

import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test ItemsServiceTest.
 *  The interface ItemsService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.ItemsService")
public class ItemsServiceImpl implements ItemsService {

	@Inject
	@Qualifier("itemsPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public ItemsOut getItems() {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			
			
			
			
			ItemsOut itemsOut = new ItemsOut();
			Items items = terminalSession.getEntity(Items.class);
			itemsOut.setItems(items);

			return itemsOut;
		} catch(RuntimeException e){
			terminalSession.disconnect();
			throw(e);
		} finally {
			terminalSessionFactory.returnSession(terminalSession);
		}
	}

}
