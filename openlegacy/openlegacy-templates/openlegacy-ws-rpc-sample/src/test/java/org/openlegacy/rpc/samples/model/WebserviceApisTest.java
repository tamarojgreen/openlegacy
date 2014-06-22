package org.openlegacy.rpc.samples.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.rpc.samples.model.Items.InnerRecord;
import org.openlegacy.rpc.samples.services.ItemDetailsWebService;
import org.openlegacy.rpc.samples.services.ItemsWebService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;
import javax.xml.ws.BindingProvider;

import junit.framework.Assert;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WebserviceApisTest {

	@Inject
	@Qualifier("itemsClient")
	private ItemsWebService itemsClient;

	@Inject
	@Qualifier("itemDetailsClient")
	private ItemDetailsWebService itemDetailsClient;

	@Test
	public void testGetItemsWebService() throws RegistryException, LoginException {
		BindingProvider bp = (BindingProvider)itemsClient;
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "user");
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "password");

		List<InnerRecord> items = itemsClient.getItems();
		assertTrue("Size of items list is less than 1", items.size() > 0);
		for (InnerRecord itemsRecord : items) {
			assertTrue("Item number is less than 1", itemsRecord.getItemNumber() > 0);
		}
	}

	@Test
	public void testGetItemDetailsWebService() throws RegistryException, LoginException {
		BindingProvider bp = (BindingProvider)itemDetailsClient;
		bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "user");
		bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "password");

		ItemDetails item = itemDetailsClient.getItem(1000);
		assertTrue("Item description is empty", !item.getItemRecord().getDescription().isEmpty());
		Assert.assertEquals("Kid Guitar", item.getItemRecord().getItemName());
		assertTrue("Item number description from ItemDetails2 is not equal to expected item number", item.getItemNum() == 1000);
	}

}
