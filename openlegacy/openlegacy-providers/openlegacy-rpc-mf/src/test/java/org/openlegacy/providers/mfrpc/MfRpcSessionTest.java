package org.openlegacy.providers.mfrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.mfrpc.mockup.RpcMFSample1;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcActions;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("mfRpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MfRpcSessionTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testRpc1() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		RpcMFSample1 entity = new RpcMFSample1();
		entity.setCaLast("Mor");
		entity.setCaName("Roi");
		entity.setCaAge(35);
		entity.setCaSalary(1000000);
		entity = rpcSession.doAction(RpcActions.EXECUTE(), entity);
		Assert.assertEquals("JANE", entity.getCaName());
		Assert.assertEquals("DOE", entity.getCaLast());
		Assert.assertEquals(new Integer(32), entity.getCaAge());
		Assert.assertEquals(new Integer(64000), entity.getCaSalary());
	}
}