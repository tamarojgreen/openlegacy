package org.openlegacy.providers.mfrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.providers.mfrpc.mockup.TestFloatEntity;
import org.openlegacy.providers.mfrpc.mockup.TestIntegerEntity;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcActions;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("mfRpcSessionTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MfRpcNumbersTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testInt() {
		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		TestIntegerEntity entity = new TestIntegerEntity();
		TestIntegerEntity.Dfhcommarea in = entity.getDfhcommarea();
		in.setTnum(49);
		in.setTnum2(-5);
		entity = rpcSession.doAction(RpcActions.EXECUTE(), entity);
		in = entity.getDfhcommarea();
		Assert.assertEquals(new Integer(98), in.getTnum());
		Assert.assertEquals(new Integer(-10), in.getTnum2());

	}

	@Test
	public void testDuble() {

		RpcSession rpcSession = applicationContext.getBean(RpcSession.class);
		TestFloatEntity entity = new TestFloatEntity();
		TestFloatEntity.Dfhcommarea in = entity.getDfhcommarea();
		in.setTnum1(12.35);
		in.setTnum2(-245.6);
		in.setTnum3(1.2);
		entity = rpcSession.doAction(RpcActions.EXECUTE(), entity);
		in = entity.getDfhcommarea();
		Assert.assertEquals(new Double(13.35), in.getTnum1());
		Assert.assertEquals(new Double(-244.6), in.getTnum2());

	}

}