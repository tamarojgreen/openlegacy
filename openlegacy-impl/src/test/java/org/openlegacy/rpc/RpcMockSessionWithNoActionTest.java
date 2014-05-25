package org.openlegacy.rpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.definitions.mock.NoActionEntity1;
import org.openlegacy.rpc.definitions.mock.NoActionEntity2;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("test-rpc-mock-session-without-action-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcMockSessionWithNoActionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testSessionTrail() throws IOException {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);

		NoActionEntity1 noActionEntity1 = rpcSession.getEntity(NoActionEntity1.class);
		Assert.assertNotNull(noActionEntity1);
		Assert.assertEquals("John", noActionEntity1.getFirstName());

		NoActionEntity2 noActionEntity2 = rpcSession.getEntity(NoActionEntity2.class);
		Assert.assertNotNull(noActionEntity2);
		Assert.assertEquals("Danny", noActionEntity2.getFirstName());

	}
}