package org.openlegacy.rpc;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.rpc.definitions.mock.ItemDetails;
import org.openlegacy.rpc.definitions.mock.Items;
import org.openlegacy.rpc.definitions.mock.Items.InnerRecord;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-demo-conntext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockDemoSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testDemoSessionTrailItems() throws IOException {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		Items items = rpcSession.getEntity(Items.class);
		Assert.assertNotNull(items);

		Assert.assertEquals(5, items.getInnerRecord().size());
		InnerRecord innerRecord = items.getInnerRecord().get(2);

		Assert.assertEquals("Water Ball", innerRecord.getItemName());
		Assert.assertEquals("Water Ball - Balls", innerRecord.getDescription());

		SessionTrail<? extends Snapshot> sessionTrail = rpcSession.getModule(Trail.class).getSessionTrail();
		Assert.assertNotNull(sessionTrail);
		Assert.assertEquals(1, sessionTrail.getSnapshots().size());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(rpcSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = StringUtil.toString(baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("itemsTrail.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());
	}

	@Test
	public void RpcMockDemoSessionItemDetailsTest() throws IOException {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		List<String> names = new ArrayList<String>();
		names.add("Kids Guitar");
		names.add("Ball Pool");
		names.add("Water Ball");
		names.add("Frisbee");
		ItemDetails itemDetails;
		for (Integer i = 1000; i < 1004; i++) {
			itemDetails = rpcSession.getEntity(ItemDetails.class);
			Assert.assertEquals(i, itemDetails.getItemNum());
			Assert.assertEquals(names.get(i - 1000), itemDetails.getItemRecord().getItemName());

		}

		SessionTrail<? extends Snapshot> sessionTrail = rpcSession.getModule(Trail.class).getSessionTrail();
		Assert.assertNotNull(sessionTrail);
		Assert.assertEquals(3, sessionTrail.getSnapshots().size());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(rpcSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = StringUtil.toString(baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("itemDetailsTrail.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());

	}

	@Ignore
	@Test
	public void rpcMockReconnect() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		Items items = rpcSession.getEntity(Items.class);
		items = rpcSession.doAction(RpcActions.READ(), items);
		ItemDetails itemDetails = rpcSession.getEntity(ItemDetails.class);
		itemDetails = rpcSession.doAction(RpcActions.READ(), itemDetails);
		rpcSession.disconnect();
		rpcSession.login("", "");
		items = rpcSession.getEntity(Items.class);
		items = rpcSession.doAction(RpcActions.READ(), items);

	}
}
