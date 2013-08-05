package org.openlegacy.rpc;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.rpc.definitions.mock.RpcDummyEntity;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-rpc-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testSessionTrail() throws IOException {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		RpcDummyEntity rpcDummyEntity = new RpcDummyEntity();
		rpcDummyEntity.setFirstName("John");
		rpcDummyEntity.setLastName("Doe");
		rpcDummyEntity.setAge(40);

		rpcDummyEntity = rpcSession.doAction(RpcActions.READ(), rpcDummyEntity);

		SessionTrail<? extends Snapshot> sessionTrail = rpcSession.getModule(Trail.class).getSessionTrail();
		Assert.assertNotNull(sessionTrail);
		Assert.assertEquals(1, sessionTrail.getSnapshots().size());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(rpcSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = StringUtil.toString(baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("trail.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());
	}
}