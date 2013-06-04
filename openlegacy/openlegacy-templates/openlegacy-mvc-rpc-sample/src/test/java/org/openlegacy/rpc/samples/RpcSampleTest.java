package org.openlegacy.rpc.samples;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.samples.model.ItemDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/META-INF/spring/applicationContext-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcSampleTest {

	@Inject
	ApplicationContext applicationContext;

	@Test
	public void testApplication() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		ItemDetails itemDetails = rpcSession.getEntity(ItemDetails.class, 123);
		Assert.assertNotNull(itemDetails);
		Assert.assertEquals("Domino cubes", itemDetails.getItemName());
		Assert.assertEquals("Dominos cubes black/red", itemDetails.getItemDescription());
		Assert.assertEquals(Integer.valueOf(5), itemDetails.getItemWeight());
	}
}
