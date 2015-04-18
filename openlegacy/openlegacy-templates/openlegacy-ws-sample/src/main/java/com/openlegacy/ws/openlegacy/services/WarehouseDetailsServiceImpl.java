package com.openlegacy.ws.openlegacy.services;

import org.openlegacy.terminal.ScreenEntity;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.inject.Inject;

import javax.jws.WebService;

import com.openlegacy.ws.openlegacy.WarehouseDetails;


import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test WarehouseDetailsServiceTest.
 *  The interface WarehouseDetailsService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.WarehouseDetailsService")
public class WarehouseDetailsServiceImpl implements WarehouseDetailsService {

	@Inject
	@Qualifier("warehouseDetailsPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public WarehouseDetailsOut getWarehouseDetails(WarehouseDetailsIn warehouseDetailsIn) {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			
			WarehouseDetails warehouseDetails = terminalSession.getEntity(WarehouseDetails.class, warehouseDetailsIn.getWarehouseNumber());
			
			
			
			WarehouseDetailsOut warehouseDetailsOut = new WarehouseDetailsOut();
			warehouseDetailsOut.setWarehouseNumber(warehouseDetails.getWarehouseNumber());
			warehouseDetailsOut.setWarehouseDescription(warehouseDetails.getWarehouseDescription());
			warehouseDetailsOut.setWarehouseType(warehouseDetails.getWarehouseType());
			warehouseDetailsOut.setWarehouseTypeName(warehouseDetails.getWarehouseTypeName());
			warehouseDetailsOut.setCostingType(warehouseDetails.getCostingType());
			warehouseDetailsOut.setAmendedDate(warehouseDetails.getAmendedDate());
			warehouseDetailsOut.setAmendedBy(warehouseDetails.getAmendedBy());
			warehouseDetailsOut.setCreatedDate(warehouseDetails.getCreatedDate());
			warehouseDetailsOut.setCreatedBy(warehouseDetails.getCreatedBy());

			return warehouseDetailsOut;
		} catch(RuntimeException e){
			terminalSession.disconnect();
			throw(e);
		} finally {
			terminalSessionFactory.returnSession(terminalSession);
		}
	}

}
