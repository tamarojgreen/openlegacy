package org.openlegacy.rpc.samples;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.ItemDetails;
import org.openlegacy.rpc.samples.model.Items;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MvcRpcSampleTest {

	@Inject
	ApplicationContext applicationContext;

	@Test
	public void testApplication() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		rpcSession.login("XXX", "YYY");

		Items items = rpcSession.getEntity(Items.class);
		Assert.assertNotNull(items);
		Assert.assertEquals(5, items.getInnerRecord().size());
		ItemDetails itemDetails = rpcSession.getEntity(ItemDetails.class, 1000);
		Assert.assertNotNull(itemDetails);
		Assert.assertEquals("Kid Guitar", itemDetails.getItemRecord().getItemName());
		Assert.assertEquals("Kids Guitar - Musical Toys", itemDetails.getItemRecord().getDescription());
		Assert.assertEquals(Integer.valueOf(200), itemDetails.getItemRecord().getWeight());
	}
}
