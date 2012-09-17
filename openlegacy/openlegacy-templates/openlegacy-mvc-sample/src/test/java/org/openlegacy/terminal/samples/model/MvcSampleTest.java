package org.openlegacy.terminal.samples.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MvcSampleTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testSession() throws RegistryException, LoginException {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		terminalSession.getModule(Login.class).login("user", "pwd");

		Items items = terminalSession.getEntity(Items.class);
		Assert.assertEquals(12, items.getItemsRecords().size());

		ItemDetails itemDetails = terminalSession.getEntity(ItemDetails.class, 2000);
		Assert.assertEquals(2000, itemDetails.getItemNumber().intValue());

		WarehouseDetails warehouseDetails = terminalSession.getEntity(WarehouseDetails.class, 3);
		Assert.assertEquals(3, warehouseDetails.getWarehouseNumber().intValue());
		Assert.assertNotNull(warehouseDetails.getAmendedDate());

	}
}
