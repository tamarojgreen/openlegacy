package com.openlegacy.ws.openlegacy.services;

import java.util.List;

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.jws.WebService;

import com.openlegacy.ws.openlegacy.Items; 
import com.openlegacy.ws.openlegacy.Items.ItemDetailesRecord;

import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test GetItemsServiceTest.
 *  The interface GetItemsService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.GetItemsService")
public class GetItemsServiceImpl implements GetItemsService {

	@Inject
	@Qualifier("getItemsPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public GetItemsOut getGetItems() {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			GetItemsOut getItemsOut = new GetItemsOut();
			Items items = terminalSession.getEntity(Items.class);
			List<ItemDetailesRecord> records = items.getItemDetailesRecords();
			getItemsOut.setItems(items);
			getItemsOut.setRecords(records);

			return getItemsOut;
		} catch(RuntimeException e){
			terminalSession.disconnect();
			throw(e);
		} finally {
			terminalSessionFactory.returnSession(terminalSession);
		}
	}

	public static class InitAction implements TerminalAction{
		
		public void perform(TerminalSession terminalSession, Object entity,Object... keys){
			terminalSession.getModule(Login.class).login("OPENLEGA1","OPENLEGA");
			// PLACE HOLDER for init action
		}

		@Override
		public boolean isMacro() {
			// TODO Auto-generated method stub
			return false;
		}
	}
	public static class KeepAliveAction implements TerminalAction{
		
		public void perform(TerminalSession terminalSession, Object entity,Object... keys){
			// PLACE HOLDER for keep alive action
		}

		@Override
		public boolean isMacro() {
			// TODO Auto-generated method stub
			return false;
		}
	}
	public static class CleanupAction implements TerminalAction{
		
		public void perform(TerminalSession terminalSession, Object entity,Object... keys){
			terminalSession.doAction(TerminalActions.F12());
			// PLACE HOLDER for cleanup action
		}

		@Override
		public boolean isMacro() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
