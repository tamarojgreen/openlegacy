package org.openlegacy.rpc;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.rpc.definitions.mock.InnerKeyEntity;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-session-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testSessionTrail() throws IOException {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		RpcDummyEntity rpcDummyEntity = rpcSession.getEntity(RpcDummyEntity.class, "John", "Doe", 40);

		Assert.assertEquals("My name is John Doe", rpcDummyEntity.getMessage());
		SessionTrail<? extends Snapshot> sessionTrail = rpcSession.getModule(Trail.class).getSessionTrail();
		Assert.assertNotNull(sessionTrail);
		Assert.assertEquals(1, sessionTrail.getSnapshots().size());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(rpcSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = StringUtil.toString(baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("trail.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());

		MenuItem menuTree = rpcSession.getModule(Menu.class).getMenuTree();
		Assert.assertEquals(2, menuTree.getMenuItems().size());
		Assert.assertEquals("Main", menuTree.getDisplayName());

		Set<String> subMenuNames = new HashSet<String>();
		subMenuNames.add(menuTree.getMenuItems().get(0).getDisplayName());
		subMenuNames.add(menuTree.getMenuItems().get(1).getDisplayName());
		Set<String> subMeneExpected = new HashSet<String>();
		subMeneExpected.add("Tree1");
		subMeneExpected.add("Inventory Menu");
		Assert.assertTrue(subMenuNames.containsAll(subMeneExpected));
	}

	@Test
	public void innerKeysTest() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		rpcSession.getEntity(InnerKeyEntity.class, "key1", "key2");
	}
}