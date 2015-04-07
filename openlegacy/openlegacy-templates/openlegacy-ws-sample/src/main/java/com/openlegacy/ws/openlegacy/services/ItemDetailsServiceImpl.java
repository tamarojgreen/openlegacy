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
 *  Can be tested by invoking the test ItemDetailsServiceTest.
 *  The interface ItemDetailsService can be customized to enabling passing parameters to the service, and this class can consume the parameters within the relavant screens.    
 */
@WebService(endpointInterface = "com.openlegacy.ws.openlegacy.services.ItemDetailsService")
public class ItemDetailsServiceImpl implements ItemDetailsService {

	@Inject
	@Qualifier("itemDetailsPool")
	private TerminalSessionFactory terminalSessionFactory;

	@Override
	public ItemDetailsOut getItemDetails(ItemDetailsIn itemDetailsIn) {

		TerminalSession terminalSession = terminalSessionFactory.getSession();		
		try{
			
			ItemDetails itemDetails = terminalSession.getEntity(ItemDetails.class, itemDetailsIn.getItemNumber());

			ItemDetailsOut itemDetailsOut = new ItemDetailsOut();
			itemDetailsOut.setItemNumber(itemDetails.getItemNumber());
			itemDetailsOut.setItemDescription(itemDetails.getItemDescription());
			itemDetailsOut.setAlphaSearch(itemDetails.getAlphaSearch());
			itemDetailsOut.setSupercedingItemto(itemDetails.getSupercedingItemto());
			itemDetailsOut.setSupercedingItemfrom(itemDetails.getSupercedingItemfrom());
			itemDetailsOut.setSubstituteItemNumber(itemDetails.getSubstituteItemNumber());
			itemDetailsOut.setManufacturersItemNo(itemDetails.getManufacturersItemNo());
			itemDetailsOut.setItemWeight(itemDetails.getItemWeight());
			itemDetailsOut.setItemClass(itemDetails.getItemClass());
			itemDetailsOut.setItemClassName(itemDetails.getItemClassName());
			itemDetailsOut.setStockGroup(itemDetails.getStockGroup());
			itemDetailsOut.setStockGroupName(itemDetails.getStockGroupName());
			itemDetailsOut.setUnitOfMeasure(itemDetails.getUnitOfMeasure());
			itemDetailsOut.setPackingMultiplier(itemDetails.getPackingMultiplier());
			itemDetailsOut.setOuterUnitOfMeasure(itemDetails.getOuterUnitOfMeasure());
			itemDetailsOut.setOuterQuantity(itemDetails.getOuterQuantity());
			itemDetailsOut.setPalletLabelRequired(itemDetails.getPalletLabelRequired());
			itemDetailsOut.setVatCode(itemDetails.getVatCode());

			return itemDetailsOut;
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
