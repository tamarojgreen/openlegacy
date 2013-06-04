package org.openlegacy.providers.jt400;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.providers.jt400.mockup.RpgStrNum;
import org.openlegacy.rpc.RpcActions;
import org.openlegacy.rpc.RpcSession;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("Jt400RpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Jt400RpcSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testJt400RpcSession() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		RpgStrNum rpcStrNum = new RpgStrNum();
		rpcStrNum.setFirstName("John");
		rpcStrNum.setLastName("Doe");
		rpcStrNum.setAge(40);
		rpcStrNum = rpcSession.doAction(RpcActions.READ(), rpcStrNum);

		SessionTrail<? extends Snapshot> trail = rpcSession.getModule(Trail.class).getSessionTrail();
		Assert.assertEquals("My name is John Doe age 40 years !", rpcStrNum.getMessage());
	}
}