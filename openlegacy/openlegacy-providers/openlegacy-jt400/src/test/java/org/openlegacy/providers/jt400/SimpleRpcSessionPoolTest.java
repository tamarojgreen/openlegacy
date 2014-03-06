package org.openlegacy.providers.jt400;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.rpc.RpcSession;
import org.openlegacy.rpc.actions.RpcAction;
import org.openlegacy.rpc.support.SimpleRpcSessionPoolFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("DefaultRpcSessionPoolTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleRpcSessionPoolTest {

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testPool() {
		final SimpleRpcSessionPoolFactory rpcSessionPool = applicationContext.getBean(SimpleRpcSessionPoolFactory.class);

		Assert.assertEquals(0, rpcSessionPool.getActives().size());
		final RpcSession rpcSession1 = rpcSessionPool.getSession();

		Assert.assertTrue(rpcSession1.isConnected());
		Assert.assertEquals(1, rpcSessionPool.getActives().size());

		@SuppressWarnings("unused")
		RpcSession rpcSession2 = rpcSessionPool.getSession();
		Assert.assertEquals(2, rpcSessionPool.getActives().size());

		Thread thread = new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw (new RuntimeException(e));
				}
				rpcSessionPool.returnSession(rpcSession1);
			};
		};
		thread.start();

		long before = System.currentTimeMillis();
		// should wait for run to end
		@SuppressWarnings("unused")
		RpcSession rpcSession3 = rpcSessionPool.getSession();
		long after = System.currentTimeMillis();

		Assert.assertTrue((after - before) >= 1000);
	}

	@Test
	public void testKeepAlive() throws InterruptedException {
		SimpleRpcSessionPoolFactory rpcSessionPool = applicationContext.getBean(SimpleRpcSessionPoolFactory.class);

		RpcSession session = rpcSessionPool.getSession();
		// session.getSnapshot();
		Assert.assertFalse(DummyAction.isCalled());

		rpcSessionPool.returnSession(session);
		Thread.sleep(4000);
		Assert.assertTrue(DummyAction.isCalled());

	}

	public static class DummyAction extends Jt400KeepAliveAction implements RpcAction {

		private static boolean called = false;

		public static boolean isCalled() {
			return called;
		}

		@Override
		public void perform(RpcSession session, Object entity, Object... keys) {
			super.perform(session, entity, keys);
			called = true;

		}

	}
}
