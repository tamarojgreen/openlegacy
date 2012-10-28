package org.openlegacy.terminal.samples.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.samples.model.Items.ItemsRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WebserviceApisTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testGetItems() throws RegistryException, LoginException {
		List<ItemsRecord> items = getItems();
		for (ItemsRecord itemsRecord : items) {
			System.out.println(itemsRecord.getItemNumber());
		}
	}

	@Test
	public void testGetItem() throws RegistryException, LoginException {
		ItemDetails itemDetails = getItem(2000);
		System.out.println(itemDetails.getItemDescription());
		System.out.println(itemDetails.getItemDetails2().getItemNumberdesc());

	}

	private ItemDetails getItem(int itemNumber) throws RegistryException, LoginException {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		terminalSession.getModule(Login.class).login("user", "pwd");
		ItemDetails itemDetails = terminalSession.getEntity(ItemDetails.class, itemNumber);
		// fetch proxy
		itemDetails.getItemDetails2();
		return itemDetails;
	}

	private List<ItemsRecord> getItems() throws RegistryException, LoginException {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		terminalSession.getModule(Login.class).login("user", "pwd");

		List<ItemsRecord> items = terminalSession.getModule(Table.class).collectAll(Items.class, ItemsRecord.class);

		return items;
	}
}
