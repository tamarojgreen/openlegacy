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

import com.openlegacy.ws.openlegacy.ItemDetails;


import org.openlegacy.terminal.wait_conditions.WaitCondition;

/**
 *  A service implementation which invokes OpenLegacy API, and returns a service output.
 *  The code below should be customize to perform a working scenario which goes through the relevant screens.
 *  Can be tested by invoking the test GetItemDetailesServiceTest.
 *  The interface GetItemDetailesService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.GetItemDetailesService")
public class GetItemDetailesServiceImpl implements GetItemDetailesService {

	@Inject
	@Qualifier("getItemDetailesPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public GetItemDetailesOut getGetItemDetailes(GetItemDetailesIn getItemDetailesIn) {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			
			ItemDetails itemDetails = terminalSession.getEntity(ItemDetails.class, getItemDetailesIn.getItemNumber());
			
			
			
			GetItemDetailesOut getItemDetailesOut = new GetItemDetailesOut();
			getItemDetailesOut.setItemNumber(itemDetails.getItemNumber());
			getItemDetailesOut.setItemDescription(itemDetails.getItemDescription());
			getItemDetailesOut.setAlphaSearch(itemDetails.getAlphaSearch());
			getItemDetailesOut.setSupercedingItemto(itemDetails.getSupercedingItemto());
			getItemDetailesOut.setSupercedingItemfrom(itemDetails.getSupercedingItemfrom());
			getItemDetailesOut.setSubstituteItemNumber(itemDetails.getSubstituteItemNumber());
			getItemDetailesOut.setManufacturersItemNo(itemDetails.getManufacturersItemNo());
			getItemDetailesOut.setItemWeight(itemDetails.getItemWeight());
			getItemDetailesOut.setItemClass(itemDetails.getItemClass());
			getItemDetailesOut.setItemClassName(itemDetails.getItemClassName());
			getItemDetailesOut.setStockGroup(itemDetails.getStockGroup());
			getItemDetailesOut.setStockGroupName(itemDetails.getStockGroupName());
			getItemDetailesOut.setUnitOfMeasure(itemDetails.getUnitOfMeasure());
			getItemDetailesOut.setPackingMultiplier(itemDetails.getPackingMultiplier());
			getItemDetailesOut.setOuterUnitOfMeasure(itemDetails.getOuterUnitOfMeasure());
			getItemDetailesOut.setOuterQuantity(itemDetails.getOuterQuantity());
			getItemDetailesOut.setPalletLabelRequired(itemDetails.getPalletLabelRequired());
			getItemDetailesOut.setVatCode(itemDetails.getVatCode());

			return getItemDetailesOut;
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
