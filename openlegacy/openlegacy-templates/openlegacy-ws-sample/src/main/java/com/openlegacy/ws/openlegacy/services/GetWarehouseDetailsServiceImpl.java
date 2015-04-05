package com.openlegacy.ws.openlegacy.services;

import org.openlegacy.terminal.ScreenEntity;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.inject.Inject;

import javax.jws.WebService;

import com.openlegacy.ws.openlegacy.WarehouseDetails;


import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test GetWarehouseDetailsServiceTest.
 *  The interface GetWarehouseDetailsService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.GetWarehouseDetailsService")
public class GetWarehouseDetailsServiceImpl implements GetWarehouseDetailsService {

	@Inject
	@Qualifier("getWarehouseDetailsPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public GetWarehouseDetailsOut getGetWarehouseDetails(GetWarehouseDetailsIn getWarehouseDetailsIn) {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			WarehouseDetails warehouseDetails = terminalSession.getEntity(WarehouseDetails.class, getWarehouseDetailsIn.getWarehouseNumber());						
			warehouseDetails.setWarehouseNumber(getWarehouseDetailsIn.getWarehouseNumber());
			
			terminalSession.doAction(TerminalActions.ENTER(), (ScreenEntity)warehouseDetails);
			
			GetWarehouseDetailsOut getWarehouseDetailsOut = new GetWarehouseDetailsOut();
			getWarehouseDetailsOut.setWarehouseNumber(warehouseDetails.getWarehouseNumber());
			getWarehouseDetailsOut.setWarehouseDescription(warehouseDetails.getWarehouseDescription());
			getWarehouseDetailsOut.setWarehouseType(warehouseDetails.getWarehouseType());
			getWarehouseDetailsOut.setWarehouseTypeName(warehouseDetails.getWarehouseTypeName());
			getWarehouseDetailsOut.setCostingType(warehouseDetails.getCostingType());
			getWarehouseDetailsOut.setAmendedDate(warehouseDetails.getAmendedDate());
			getWarehouseDetailsOut.setAmendedBy(warehouseDetails.getAmendedBy());
			getWarehouseDetailsOut.setCreatedDate(warehouseDetails.getCreatedDate());
			getWarehouseDetailsOut.setCreatedBy(warehouseDetails.getCreatedBy());

			return getWarehouseDetailsOut;
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
