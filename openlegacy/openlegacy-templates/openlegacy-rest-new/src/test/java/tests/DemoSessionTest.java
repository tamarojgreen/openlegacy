package tests;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test1.SignOn;  
import com.test1.MainMenu;  
import com.test1.InventoryMenu;  
import com.test1.Items;  
import com.test1.ItemDetails;  
import com.test1.StockDetails;  
import com.test1.Warehouses;  
import com.test1.WarehouseDetails;  
import com.test1.WarehouseTypes;  

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoSessionTest {

	@Inject
	private TerminalSession terminalSession;
	
	@Test
	public void testSession(){

		 SignOn signOn = terminalSession.getEntity(SignOn.class);
		 signOn.setUser("someuser");
		 signOn.setPassword("somepwd");
		 MainMenu mainMenu = terminalSession.doAction(TerminalActions.ENTER(),signOn,MainMenu.class);
		 mainMenu.setMenuSelection(1);
		 InventoryMenu inventoryMenu = terminalSession.doAction(TerminalActions.ENTER(),mainMenu,InventoryMenu.class);
		 inventoryMenu.setMenuSelection(1);
		 Items items = terminalSession.doAction(TerminalActions.ENTER(),inventoryMenu,Items.class);
		 ItemDetails itemDetails = terminalSession.doAction(TerminalActions.PAGEDOWN(),items,ItemDetails.class);
		 StockDetails stockDetails = terminalSession.doAction(TerminalActions.ENTER(),itemDetails,StockDetails.class);
		 WarehouseDetails warehouseDetails = terminalSession.doAction(TerminalActions.ENTER(),stockDetails,WarehouseDetails.class);
		 WarehouseTypes warehouseTypes = terminalSession.doAction(TerminalActions.F4(),warehouseDetails,WarehouseTypes.class);
				
	}
}
